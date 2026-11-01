package com.example.retrofit.features.detalle

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.retrofit.domain.model.Habilidad
import com.example.retrofit.domain.model.Pokemon
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    pokemon: Pokemon?,
    error: String?,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    Log.d("DETALLE", "Pantalla: pokemon=${pokemon?.nombre}, error=$error")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemon?.nombre?.replaceFirstChar { it.uppercase() } ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            // Si la carga falló, mostramos el error y un botón para reintentar
            error != null -> Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(error, color = Color(0xFFC62828))
                Button(onClick = onReintentar) {
                    Text("Reintentar")
                }
            }

            // Mientras no llega el Pokémon, mostramos un indicador de carga
            pokemon == null -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            // Pokémon cargado: mostramos toda la información
            else -> Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "#${pokemon.id}  ${pokemon.nombre.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.headlineMedium
                )

                pokemon.imagenUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = pokemon.nombre,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Text("Tipo: ${pokemon.tipo}${pokemon.tipo2?.let { " / $it" } ?: ""}")
                Text("Generación: ${pokemon.generacion}")

                Text("Estadísticas", style = MaterialTheme.typography.titleMedium)
                FilaEstadistica("Vida", pokemon.estadisticas.vida)
                FilaEstadistica("Ataque", pokemon.estadisticas.ataque)
                FilaEstadistica("Ataque especial", pokemon.estadisticas.ataque_especial)
                FilaEstadistica("Defensa", pokemon.estadisticas.defensa)
                FilaEstadistica("Defensa especial", pokemon.estadisticas.defensa_especial)
                FilaEstadistica("Velocidad", pokemon.estadisticas.velocidad)

                Text("Habilidades", style = MaterialTheme.typography.titleMedium)
                pokemon.habilidad?.let { TarjetaHabilidad(it) }
                pokemon.habilidad2?.let { TarjetaHabilidad(it) }
                pokemon.oculta?.let { TarjetaHabilidad(it) }
            }
        }
    }
}

@Composable
fun FilaEstadistica(nombre: String, valor: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(nombre)
        Text(valor.toString(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TarjetaHabilidad(habilidad: Habilidad) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            val etiqueta = if (habilidad.esOculta) " (oculta)" else ""
            Text(
                text = habilidad.nombre.replaceFirstChar { it.uppercase() } + etiqueta,
                fontWeight = FontWeight.Bold
            )
            Text(habilidad.efecto)
        }
    }
}