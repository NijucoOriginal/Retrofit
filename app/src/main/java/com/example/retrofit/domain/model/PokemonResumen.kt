package com.example.retrofit.domain.model

data class PokemonResumen(
    val id: Int,
    val nombre: String,
    val imagenUrl: String? = null
)