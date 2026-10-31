package com.example.retrofit.domain.usecase

import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.repository.PokemonRepository
import javax.inject.Inject

class ObtenerPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(nombre: String): Pokemon {
        return pokemonRepository.obtenerPokemon(nombre.trim().lowercase())
    }
}