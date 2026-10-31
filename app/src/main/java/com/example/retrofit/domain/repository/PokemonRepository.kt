package com.example.retrofit.domain.repository

import com.example.retrofit.data.repository.PokemonDto

interface PokemonRepository {

    suspend fun solicitarPokemon(name: String): PokemonDto
}