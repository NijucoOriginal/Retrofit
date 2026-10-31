package com.example.retrofit.domain.model

data class Pokemon(val id: Int,
                   var nombre: String,
                   var tipo: Tipo,
                   var tipo2: Tipo,
                   var habilidad:Habilidad,
                   var habilidad2: Habilidad,
                   var oculta: Habilidad,
                   var estadisticas: Estadisticas,
                   val generacion: Generacion) {
}