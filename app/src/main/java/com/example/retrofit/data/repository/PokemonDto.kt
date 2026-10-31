package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Estadisticas
import com.example.retrofit.domain.model.Generacion
import com.example.retrofit.domain.model.Habilidad
import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.model.Tipo
import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int = 0,
    val name: String = "",
    val types: List<TipoDto> = listOf(),
    val abilities: List<HabilidadPokemonDto> = listOf(),
    val stats: List<EstadisticasDto> = listOf(),
    val species: Referencia? = null
) {

    // Recibe las habilidades ya convertidas por el repositorio
    fun toDomain(generacion: Referencia?, habilidades: List<Habilidad>): Pokemon {
        val normales = habilidades.filter { !it.esOculta }

        return Pokemon(
            id = id,
            nombre = name,
            tipo = types.firstOrNull()?.let { convertirTipo(it) } ?: Tipo.NORMAL,
            tipo2 = types.getOrNull(1)?.let { convertirTipo(it) },
            habilidad = normales.getOrNull(0),
            habilidad2 = normales.getOrNull(1),
            oculta = habilidades.firstOrNull { it.esOculta },
            estadisticas = convertirEstadisticas(stats),
            generacion = traducirGeneracion(generacion)
        )
    }

    private fun convertirTipo(tipo: TipoDto): Tipo? = when (tipo.type?.name) {
        "fire" -> Tipo.FUEGO
        "water" -> Tipo.AGUA
        "steel" -> Tipo.ACERO
        "bug" -> Tipo.BICHO
        "dragon" -> Tipo.DRAGON
        "electric" -> Tipo.ELECTRICO
        "ghost" -> Tipo.FANTASMA
        "fairy" -> Tipo.HADA
        "ice" -> Tipo.HIELO
        "fighting" -> Tipo.LUCHA
        "normal" -> Tipo.NORMAL
        "grass" -> Tipo.PLANTA
        "psychic" -> Tipo.PSIQUICO
        "rock" -> Tipo.ROCA
        "dark" -> Tipo.SINIESTRO
        "ground" -> Tipo.TIERRA
        "flying" -> Tipo.VOLADOR
        "poison" -> Tipo.VENENO
        else -> null
    }

    private fun convertirEstadisticas(stats: List<EstadisticasDto>): Estadisticas {
        // Mapa de nombre de la API -> valor base
        val valores = stats.associate { (it.stat?.name ?: "") to it.baseStat }

        return Estadisticas(
            vida = valores["hp"] ?: 0,
            ataque = valores["attack"] ?: 0,
            ataque_especial = valores["special-attack"] ?: 0,
            defensa = valores["defense"] ?: 0,
            defensa_especial = valores["special-defense"] ?: 0,
            velocidad = valores["speed"] ?: 0
        )
    }
}