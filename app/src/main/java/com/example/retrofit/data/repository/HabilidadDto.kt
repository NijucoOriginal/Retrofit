package com.example.retrofit.data.repository

data class HabilidadDto(var id: Int = 0,
                        var name: String = "",
                        var effect: EfectoDto? = null,
                        var generation: String = "",
                        var is_hidden: Boolean = false) {
}