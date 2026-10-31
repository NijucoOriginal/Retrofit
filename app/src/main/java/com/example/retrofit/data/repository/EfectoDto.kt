package com.example.retrofit.data.repository

import com.google.gson.annotations.SerializedName

data class EfectoDto(
    @SerializedName("short_effect") val shortEffect: String = "",
    val language: Referencia? = null
)