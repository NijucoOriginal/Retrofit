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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val monitorConexion: MonitorConexion
) : ViewModel() {

    // Se declara antes del bloque init para que ya esté inicializada
    val hayConexion: StateFlow<Boolean> = monitorConexion.hayConexion

    var pokemones by mutableStateOf<List<PokemonResumen>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

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

        // Si vuelve la conexión y la última carga falló, reintentamos sola
        viewModelScope.launch {
            hayConexion.collect { conectado ->
                if (conectado && error != null && !hayFiltros()) {
                    cargarSiguientePagina()
                }
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

    // Lista general paginada. Solo se usa cuando no hay filtros activos
    fun cargarSiguientePagina() {
        if (cargando || !hayMas || hayFiltros()) return

        trabajo = viewModelScope.launch {
            cargando = true
            error = null
            try {
                val nuevos = repository.obtenerListaPokemon(LIMITE, offset)
                pokemones = pokemones + nuevos
                offset += LIMITE
                hayMas = nuevos.size == LIMITE
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "No se pudo cargar la lista. Revisa tu conexión."
            } finally {
                cargando = false
            }
        }
    }

    private fun aplicarFiltros(conRetraso: Boolean) {
        // Solo cuenta el filtro más reciente
        trabajo?.cancel()
        cargando = false
        error = null

        val textoLimpio = texto.trim().lowercase()
        val tipoActual = tipo

        when {
            // Sin filtros: volvemos a la lista general desde el principio
            textoLimpio.isEmpty() && tipoActual == null -> {
                pokemones = emptyList()
                offset = 0
                hayMas = true
                cargarSiguientePagina()
            }

            // Con tipo: pedimos sus Pokémon y filtramos por texto en local
            tipoActual != null -> {
                trabajo = viewModelScope.launch {
                    cargando = true
                    try {
                        val lista = cacheTipo
                            ?.takeIf { it.first == tipoActual }
                            ?.second
                            ?: repository.obtenerPokemonesPorTipo(tipoActual)
                                .also { cacheTipo = tipoActual to it }

                        pokemones = lista.filter { it.nombre.contains(textoLimpio) }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        pokemones = emptyList()
                        error = "No se pudieron cargar los Pokémon de ese tipo."
                    } finally {
                        cargando = false
                    }
                }
            }

            // Solo texto: búsqueda exacta por nombre, con retraso para no pedir en cada letra
            else -> {
                trabajo = viewModelScope.launch {
                    if (conRetraso) delay(400)
                    cargando = true
                    try {
                        pokemones = listOf(repository.buscarResumen(textoLimpio))
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        pokemones = emptyList()
                        error = "No se encontró ningún Pokémon llamado \"$textoLimpio\"."
                    } finally {
                        cargando = false
                    }
                }
            }
        }
    }

    private fun hayFiltros(): Boolean = texto.isNotBlank() || tipo != null

    companion object {
        const val LIMITE = 20
    }
}