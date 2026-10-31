package com.example.retrofit.domain.repository

import com.example.retrofit.data.repository.HabilidadDto
import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.Referencia
import com.example.retrofit.domain.model.Pokemon
import retrofit2.http.Path

interface PokemonRepository {

    suspend fun solicitarPokemon(name: String): Pokemon
}