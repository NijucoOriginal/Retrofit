package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Habilidad
import com.google.gson.annotations.SerializedName

data class HabilidadDto(
    val name: String = "",
    val generation: Referencia? = null,
    @SerializedName("effect_entries") val effectEntries: List<EfectoDto> = listOf(),
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<TextoDto> = listOf()
) {

    fun toDomain(esOculta: Boolean): Habilidad = Habilidad(
        nombre = name,
        efecto = obtenerEfecto(),
        generacion = traducirGeneracion(generation),
        esOculta = esOculta
    )

    // 1. Efecto técnico en español, 2. texto de juego en español, 3. inglés como último recurso
    private fun obtenerEfecto(): String {
        effectEntries.firstOrNull { it.language?.name == "es" }
            ?.shortEffect?.let { return it }

        flavorTextEntries.firstOrNull { it.language?.name == "es" }
            ?.flavorText?.let { return it.replace("\n", " ") }

        return effectEntries.firstOrNull { it.language?.name == "en" }
            ?.shortEffect ?: "No hay descripción del efecto"
    }


}