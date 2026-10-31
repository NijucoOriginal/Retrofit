package com.example.retrofit.data.repository

import com.google.gson.annotations.SerializedName

data class TextoDto(
    @SerializedName("flavor_text") val flavorText: String = "",
    val language: Referencia? = null
)