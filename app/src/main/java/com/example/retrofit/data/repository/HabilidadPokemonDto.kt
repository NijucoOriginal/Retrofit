package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Generacion
import com.example.retrofit.domain.model.Habilidad
import com.google.gson.annotations.SerializedName

data class HabilidadPokemonDto(val ability: Referencia? = null,
                               @SerializedName("is_hidden") val isHidden: Boolean = false,
                               val slot: Int = 0)  {

    fun toDomain(habilidad: HabilidadDto): Habilidad = Habilidad(
        nombre = ability!!.name,
        efecto = obtenerEfectoEspaniol(habilidad.effectEntries),
        generacion = determinarGeneracion(habilidad.generation),
        esOculta = isHidden
    )

    fun obtenerEfectoEspaniol(efectoDto: List<EfectoDto>): String {
        efectoDto.forEach  { efecto ->
            if(efecto.language.name=="es")
            {
                return efecto.language.name
            }
        }
        return "No hay descripción del efecto en español"
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