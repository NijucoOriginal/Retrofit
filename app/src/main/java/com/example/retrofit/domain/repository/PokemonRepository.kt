package com.example.retrofit.domain.repository

import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.model.PokemonResumen
import com.example.retrofit.domain.model.Tipo

interface PokemonRepository {

    suspend fun obtenerPokemon(name: String): Pokemon

    suspend fun obtenerListaPokemon(limit: Int, offset: Int): List<PokemonResumen>

    suspend fun buscarResumen(nombre: String): PokemonResumen

    suspend fun obtenerPokemonesPorTipo(tipo: Tipo): List<PokemonResumen>
}