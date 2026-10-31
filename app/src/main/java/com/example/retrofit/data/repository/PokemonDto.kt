package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Pokemon

data class PokemonDto(val id: Int = 0,
                      var name: String = "",
                      var types: List<TipoDto> = listOf(),
                      var abilities : List<HabilidadDto> =  listOf(),
                      var stats: List<EstadisticasDto> =  listOf(),
                      var generation: String = "",
    ) {

    fun toDomain(): Pokemon = Pokemon (
        id = id,
        nombre = name,
        tipo = types.get(0),

    )
}