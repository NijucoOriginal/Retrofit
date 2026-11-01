package com.example.retrofit.features.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofit.core.network.MonitorConexion
import com.example.retrofit.domain.model.PokemonResumen
import com.example.retrofit.domain.model.Tipo
import com.example.retrofit.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val monitorConexion: MonitorConexion
) : ViewModel() {

    val hayConexion: StateFlow<Boolean> = monitorConexion.hayConexion

    private val _uiState = MutableStateFlow<ListaUiState>(ListaUiState.Cargando)
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    // Lo que escribe el usuario: no es un resultado, así que no va en uiState
    var texto by mutableStateOf("")
        private set

    var tipo by mutableStateOf<Tipo?>(null)
        private set

    private var offset = 0
    private var hayMas = true
    private var trabajo: Job? = null

    // Última lista pedida para un tipo, para no volver a llamar a la red al escribir
    private var cacheTipo: Pair<Tipo, List<PokemonResumen>>? = null

    init {
        cargarSiguientePagina()

        // Si vuelve la conexión y la lista había fallado, reintentamos sola
        viewModelScope.launch {
            hayConexion.collect { conectado ->
                if (conectado && necesitaReintento() && !hayFiltros()) reintentar()
            }
        }
    }

    fun cambiarTexto(nuevo: String) {
        texto = nuevo
        aplicarFiltros(conRetraso = true)
    }

    fun cambiarTipo(nuevo: Tipo?) {
        tipo = nuevo
        aplicarFiltros(conRetraso = false)
    }

    // Botón "Reintentar": repite lo que falló según el estado actual
    fun reintentar() {
        when {
            hayFiltros() -> aplicarFiltros(conRetraso = false)
            _uiState.value is ListaUiState.Exito -> cargarSiguientePagina()
            else -> reiniciar()
        }
    }

    // Scroll infinito: pide la siguiente página de la lista general
    fun cargarSiguientePagina() {
        if (!hayMas || hayFiltros() || trabajo?.isActive == true) return

        val actual = _uiState.value
        val anteriores = (actual as? ListaUiState.Exito)?.pokemones.orEmpty()

        _uiState.value = if (actual is ListaUiState.Exito) {
            actual.copy(cargandoMas = true, errorMas = null)
        } else {
            ListaUiState.Cargando
        }

        trabajo = viewModelScope.launch {
            try {
                val nuevos = repository.obtenerListaPokemon(LIMITE, offset)
                offset += LIMITE
                hayMas = nuevos.size == LIMITE
                _uiState.value = ListaUiState.Exito(pokemones = anteriores + nuevos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = if (anteriores.isEmpty()) {
                    ListaUiState.Error("No se pudo cargar la lista. Revisa tu conexión.")
                } else {
                    // Conservamos lo que ya se ve y solo avisamos del fallo
                    ListaUiState.Exito(
                        pokemones = anteriores,
                        errorMas = "No se pudieron cargar más Pokémon."
                    )
                }
            }
        }
    }

    // Vuelve a la lista general desde la primera página
    private fun reiniciar() {
        trabajo?.cancel()
        offset = 0
        hayMas = true
        _uiState.value = ListaUiState.Cargando
        cargarSiguientePagina()
    }

    private fun aplicarFiltros(conRetraso: Boolean) {
        // Solo cuenta el filtro más reciente
        trabajo?.cancel()

        val textoLimpio = texto.trim().lowercase()
        val tipoActual = tipo

        when {
            textoLimpio.isEmpty() && tipoActual == null -> reiniciar()

            // Con tipo: pedimos sus Pokémon y filtramos por texto en local
            tipoActual != null -> {
                _uiState.value = ListaUiState.Cargando
                trabajo = viewModelScope.launch {
                    try {
                        val lista = cacheTipo
                            ?.takeIf { it.first == tipoActual }
                            ?.second
                            ?: repository.obtenerPokemonesPorTipo(tipoActual)
                                .also { cacheTipo = tipoActual to it }

                        // Una lista vacía aquí significa "sin resultados", no un error
                        _uiState.value = ListaUiState.Exito(
                            pokemones = lista.filter { it.nombre.contains(textoLimpio) }
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _uiState.value = ListaUiState.Error(
                            "No se pudieron cargar los Pokémon de ese tipo."
                        )
                    }
                }
            }

            // Solo texto: búsqueda exacta por nombre, con retraso para no pedir en cada letra
            else -> {
                _uiState.value = ListaUiState.Cargando
                trabajo = viewModelScope.launch {
                    if (conRetraso) delay(400)
                    try {
                        _uiState.value = ListaUiState.Exito(
                            pokemones = listOf(repository.buscarResumen(textoLimpio))
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _uiState.value = ListaUiState.Error(
                            "No se encontró ningún Pokémon llamado \"$textoLimpio\"."
                        )
                    }
                }
            }
        }
    }

    private fun necesitaReintento(): Boolean = when (val estado = _uiState.value) {
        is ListaUiState.Error -> true
        is ListaUiState.Exito -> estado.errorMas != null
        ListaUiState.Cargando -> false
    }

    private fun hayFiltros(): Boolean = texto.isNotBlank() || tipo != null

    companion object {
        const val LIMITE = 20
    }
}