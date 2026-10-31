package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.Referencia
import com.example.retrofit.domain.repository.PokemonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiservice: ApiService
): PokemonRepository {

    override suspend fun solicitarPokemon(name: String): PokemonDto {
        return apiservice.solicitarPokemon(name)
    }

    override suspend fun solicitarGeneracionPokemon(name: String): Referencia {
        return apiservice.solicitarGeneracionPokemon(name)
    }
}