package com.example.retrofit.domain.usercase

import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(nombre: String): Pokemon {
        return pokemonRepository.solicitarPokemon(nombre.trim().lowercase())
    }
}