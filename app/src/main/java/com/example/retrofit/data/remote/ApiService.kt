package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.PokemonDto
import retrofit2.http.GET

interface ApiService {
    @GET("https://pokeapi.co/api/v2/pokemon/{name}")
    suspend fun solicitarPokemon(name: String): PokemonDto
}