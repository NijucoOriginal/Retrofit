package com.example.retrofit.domain.model

data class Habilidad(var nombre: String,
                     var efecto: String,
                     val generacion: Generacion,
                     var esOculta: Boolean) {
}