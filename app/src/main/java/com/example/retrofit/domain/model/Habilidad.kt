package com.example.retrofit.domain.model

data class Habilidad(val id: Int,
                     var nombre: String,
                     var efecto: String,
                     val generacion: Generacion,
                     var esOculta: Boolean) {
}