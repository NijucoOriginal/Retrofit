package com.example.retrofit.data.repository

import com.google.gson.annotations.SerializedName

data class EstadisticasDto(@SerializedName("base_stat") val baseStat: Int = 0,
                           val stat: Referencia? = null) {
}