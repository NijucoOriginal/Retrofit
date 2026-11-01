package com.example.retrofit.data.remote

import com.example.retrofit.data.repository.traducirGeneracion
import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.model.PokemonResumen
import com.example.retrofit.domain.model.Tipo
import com.example.retrofit.domain.repository.PokemonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository {

    override suspend fun obtenerPokemon(name: String): Pokemon {
        val pokemonDto = apiService.solicitarPokemon(name)
        val especie = apiService.solicitarEspecie(pokemonDto.species!!.name)

        val habilidades = pokemonDto.abilities
            .sortedBy { it.slot }
            .map { habilidadPokemon ->
                val info = apiService.solicitarInformacionHabilidad(habilidadPokemon.ability!!.name)
                info.toDomain(esOculta = habilidadPokemon.isHidden)
            }

        return pokemonDto.toDomain(
            generacion = especie.generation,
            habilidades = habilidades
        )
    }

    override suspend fun obtenerListaPokemon(limit: Int, offset: Int): List<PokemonResumen> {
        return apiService.solicitarListaPokemon(limit, offset).results.map { ref ->
            val id = idDesdeUrl(ref.url)
            PokemonResumen(
                id = id,
                nombre = ref.name,
                imagenUrl = urlSprite(id)
            )
        }
    }

    override suspend fun buscarResumen(nombre: String): PokemonResumen {
        val dto = apiService.solicitarPokemon(nombre)
        return PokemonResumen(
            id = dto.id,
            nombre = dto.name,
            imagenUrl = urlSprite(dto.id)
        )
    }

    override suspend fun obtenerPokemonesPorTipo(tipo: Tipo): List<PokemonResumen> {
        return apiService.solicitarPokemonesPorTipo(tipo.nombreApi())
            .pokemon
            .mapNotNull { elemento ->
                elemento.pokemon?.let { ref ->
                    val id = idDesdeUrl(ref.url)
                    PokemonResumen(
                        id = id,
                        nombre = ref.name,
                        imagenUrl = urlSprite(id)
                    )
                }
            }
    }

    private fun idDesdeUrl(url: String): Int =
        url.trimEnd('/').substringAfterLast('/').toInt()

    // Mismo patrón que sprites.front_default, pero calculado a partir del id
    private fun urlSprite(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

    private fun Tipo.nombreApi(): String = when (this) {
        Tipo.ACERO -> "steel"
        Tipo.AGUA -> "water"
        Tipo.BICHO -> "bug"
        Tipo.DRAGON -> "dragon"
        Tipo.ELECTRICO -> "electric"
        Tipo.FANTASMA -> "ghost"
        Tipo.FUEGO -> "fire"
        Tipo.HADA -> "fairy"
        Tipo.HIELO -> "ice"
        Tipo.LUCHA -> "fighting"
        Tipo.NORMAL -> "normal"
        Tipo.PLANTA -> "grass"
        Tipo.PSIQUICO -> "psychic"
        Tipo.ROCA -> "rock"
        Tipo.SINIESTRO -> "dark"
        Tipo.TIERRA -> "ground"
        Tipo.VENENO -> "poison"
        Tipo.VOLADOR -> "flying"
    }
}