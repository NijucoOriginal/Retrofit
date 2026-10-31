package com.example.retrofit.data.remote

import com.example.retrofit.domain.model.Habilidad
import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.repository.PokemonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiservice: ApiService
): PokemonRepository {

    override suspend fun solicitarPokemon(name: String): Pokemon {
        val pokemonDto =apiservice.solicitarPokemon(name)
        val especieDto =apiservice.solicitarGeneracionPokemon(pokemonDto.species!!.name)

        val habilidades: MutableList<Habilidad> = mutableListOf()

        pokemonDto.abilities.forEach { habilidadPokemon ->
            val busquedaPokemon = apiservice.solicitarInformacionHabilidad(habilidadPokemon.ability!!.name)
            habilidades.add(habilidadPokemon.toDomain(busquedaPokemon))
        }

        return pokemonDto.toDomain(especieDto.generation,habilidades)




    }

}