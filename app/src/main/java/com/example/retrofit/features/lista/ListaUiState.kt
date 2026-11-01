package com.example.retrofit.features.lista

import com.example.retrofit.domain.model.PokemonResumen

sealed interface ListaUiState {

    // Primera carga, o cambio de filtro en curso
    data object Cargando : ListaUiState

    // Hay datos que mostrar. Si se está cargando otra página o esa carga falló,
    // la lista se conserva y solo cambian estos dos campos
    data class Exito(
        val pokemones: List<PokemonResumen>,
        val cargandoMas: Boolean = false,
        val errorMas: String? = null
    ) : ListaUiState

    // Falló la carga y no hay nada que mostrar
    data class Error(val mensaje: String) : ListaUiState
}