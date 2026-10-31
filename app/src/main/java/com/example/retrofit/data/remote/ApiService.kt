package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.EspecieDto
import com.example.retrofit.data.repository.HabilidadDto
import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.Referencia
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon/{name}")
    suspend fun solicitarPokemon(@Path("name") name: String): PokemonDto

    @GET("pokemon-species/{name}")
    suspend fun solicitarGeneracionPokemon(@Path("name") name: String): EspecieDto

    @GET("ability/{name}")
    suspend fun solicitarInformacionHabilidad(@Path("name") name: String): HabilidadDto


}