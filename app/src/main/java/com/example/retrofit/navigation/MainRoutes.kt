package com.example.retrofit.navigation

import kotlinx.serialization.Serializable

sealed class MainRoutes {

    @Serializable
    data object Lista : MainRoutes()

    // Recibe el nombre del Pokémon como parámetro de la ruta
    @Serializable
    data class Detalle(val nombre: String) : MainRoutes()
}