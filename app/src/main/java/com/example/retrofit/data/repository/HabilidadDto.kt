package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Generacion
import com.example.retrofit.domain.model.Habilidad
import com.google.gson.annotations.SerializedName

data class HabilidadDto(
    val name: String = "",
    val generation: Referencia? = null,
    @SerializedName("effect_entries") val effectEntries: List<EfectoDto> = listOf(),
) {

}