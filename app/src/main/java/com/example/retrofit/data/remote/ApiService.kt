package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.HabilidadDto
import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.Referencia
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("https://pokeapi.co/api/v2/pokemon/{name}")
    suspend fun solicitarPokemon(@Path("name") name: String): PokemonDto

    @GET("https://pokeapi.co/api/v2/pokemon-species/{name}")
    suspend fun solicitarGeneracionPokemon(@Path("name") name: String): Referencia

    @GET("https://pokeapi.co/api/v2/ability/{name}")
    suspend fun solicitarInformacionHabilidad(@Path("name") name: String): HabilidadDto
}