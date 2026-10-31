package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Estadisticas
import com.example.retrofit.domain.model.Generacion
import com.example.retrofit.domain.model.Habilidad
import com.example.retrofit.domain.model.Pokemon
import com.example.retrofit.domain.model.Tipo

data class PokemonDto(val id: Int = 0,
                      var name: String = "",
                      var types: List<TipoDto> = listOf(),
                      var abilities : List<HabilidadPokemonDto> =  listOf(),
                      var stats: List<EstadisticasDto> =  listOf(),
                      var species: Referencia? = null
    ) {

    fun toDomain(generacion: Referencia?, habilidades: MutableList<Habilidad>): Pokemon = Pokemon (
        id = id,
        nombre = name,
        tipo = convertirTipo(types.first()),
        tipo2 = convertirTipo(types.get(1)),
        habilidad = retornarSlotCorrecto(habilidades,1),
        habilidad2 = retornarSlotCorrecto(habilidades,2),
        oculta = retornarOculta(habilidades),
        estadisticas = convertirEstadistica(stats),
        generacion = determinarGeneracion(generacion)
    )
    fun convertirTipo(type : TipoDto): Tipo? {
        val tipo : Tipo? = when(type.type) {
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
            else -> {
                null
            }
        }
        return tipo
    }

    fun retornarOculta(habilidades: MutableList<Habilidad>): Habilidad? {
        habilidades.forEach { habilidad ->
            if(habilidad.esOculta)
            {
                return habilidad
            }
        }
        return null
    }

    fun retornarSlotCorrecto(habilidades: MutableList<Habilidad>,slot: Int): Habilidad? {
        abilities.forEach { ability ->
            habilidades.forEach { habilidad ->
                if(ability.ability!!.name==habilidad.nombre)
                {
                    if(ability.slot==slot)
                    {
                        return habilidad
                    }
                }
            }
        }
        return null
    }

    fun convertirEstadistica(stats: List<EstadisticasDto>): Estadisticas {
        var vida: Int = 0
        var ataque: Int = 0
        var ataque_especial: Int =0
        var defensa: Int = 0
        var defensa_especial: Int = 0
        var velocidad: Int = 0

        stats.forEach { estadistica ->
            when(estadistica.stat?.name)
            {
                "hp" -> vida = estadistica.baseStat
                "attack" -> ataque = estadistica.baseStat
                "defense" -> defensa = estadistica.baseStat
                "special_attack" -> ataque_especial = estadistica.baseStat
                "special_defense" -> defensa_especial = estadistica.baseStat
                "speed" -> velocidad = estadistica.baseStat
            }
        }
        val estadisticas = Estadisticas(vida = vida,
                                       ataque = ataque,
                                       ataque_especial = ataque_especial,
                                       defensa = defensa,
                                       defensa_especial = defensa_especial,
                                       velocidad = velocidad)

        return estadisticas
    }

    fun determinarGeneracion(generation: Referencia?): Generacion {
        return when(generation?.name) {
            "generation-i" -> Generacion.PRIMERA
            "generation-ii" -> Generacion.SEGUNDA
            "generation-iii" -> Generacion.TERCERA
            "generation-iv" -> Generacion.CUARTA
            "generation-v" -> Generacion.QUINTA
            "generation-vi" -> Generacion.SEXTA
            "generation-vii" -> Generacion.SEPTIMA
            "generation-viii" -> Generacion.OCTAVA
            else -> {
                Generacion.NOVENA
            }
        }
    }
}