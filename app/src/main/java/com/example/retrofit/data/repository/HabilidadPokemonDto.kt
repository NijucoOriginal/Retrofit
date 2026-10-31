package com.example.retrofit.data.repository

import com.google.gson.annotations.SerializedName

data class HabilidadPokemonDto(
    val ability: Referencia? = null,
    @SerializedName("is_hidden") val isHidden: Boolean = false,
    val slot: Int = 0
)