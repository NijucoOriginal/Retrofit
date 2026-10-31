package com.example.retrofit.features.detalle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.usecase.ObtenerPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val obtenerPokemon: ObtenerPokemonUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // "nombre" debe coincidir con el nombre del argumento en la ruta
    private val nombre: String = checkNotNull(savedStateHandle["nombre"])

    var pokemon by mutableStateOf<Pokemon?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            Log.d("DETALLE", "Pidiendo pokemon: '$nombre'")
            error = null
            try {
                val resultado = obtenerPokemon(nombre)
                Log.d("DETALLE", "Pokemon recibido: ${resultado.nombre}, id=${resultado.id}")
                pokemon = resultado
            } catch (e: Exception) {
                Log.e("DETALLE", "Error al cargar $nombre", e)
                error = "No se pudo cargar el detalle de $nombre."
            }
        }
    }
}