package com.example.retrofit.data.repository

import com.example.retrofit.domain.model.Generacion

fun traducirGeneracion(referencia: Referencia?): Generacion = when (referencia?.name) {
    "generation-i" -> Generacion.PRIMERA
    "generation-ii" -> Generacion.SEGUNDA
    "generation-iii" -> Generacion.TERCERA
    "generation-iv" -> Generacion.CUARTA
    "generation-v" -> Generacion.QUINTA
    "generation-vi" -> Generacion.SEXTA
    "generation-vii" -> Generacion.SEPTIMA
    "generation-viii" -> Generacion.OCTAVA
    else -> Generacion.NOVENA
}