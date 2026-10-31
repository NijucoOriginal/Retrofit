package com.example.retrofit.domain.repository

import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.Referencia

interface PokemonRepository {

    suspend fun solicitarPokemon(name: String): PokemonDto

    suspend fun solicitarGeneracionPokemon(name: String): Referencia
}