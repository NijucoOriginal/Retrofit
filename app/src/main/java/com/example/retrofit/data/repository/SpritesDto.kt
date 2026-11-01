package com.example.retrofit.data.repository

import com.google.gson.annotations.SerializedName

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String? = null
)