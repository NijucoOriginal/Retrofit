package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.EspecieDto
import com.example.retrofit.data.repository.HabilidadDto
import com.example.retrofit.data.repository.ListaPokemonDto
import com.example.retrofit.data.repository.PokemonDto
import com.example.retrofit.data.repository.TipoDetalleDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon/{name}")
    suspend fun solicitarPokemon(@Path("name") name: String): PokemonDto

    @GET("pokemon-species/{name}")
    suspend fun solicitarEspecie(@Path("name") name: String): EspecieDto

    @GET("ability/{name}")
    suspend fun solicitarInformacionHabilidad(@Path("name") name: String): HabilidadDto

    @GET("pokemon")
    suspend fun solicitarListaPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ListaPokemonDto

    @GET("type/{tipo}")
    suspend fun solicitarPokemonesPorTipo(@Path("tipo") tipo: String): TipoDetalleDto
}