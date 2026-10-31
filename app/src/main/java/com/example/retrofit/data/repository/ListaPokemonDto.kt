package com.example.retrofit.data.repository

data class ListaPokemonDto(
    val count: Int = 0,
    val results: List<Referencia> = listOf()
)