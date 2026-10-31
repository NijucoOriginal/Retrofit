package com.example.retrofit.features.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.retrofit.domain.model.PokemonResumen
import com.example.retrofit.domain.model.Tipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    pokemones: List<PokemonResumen>,
    textoBusqueda: String,
    onTextoBusquedaChange: (String) -> Unit,
    tipoSeleccionado: Tipo?,
    onTipoSeleccionado: (Tipo?) -> Unit,
    hayConexion: Boolean,
    cargando: Boolean,
    error: String?,
    onFinalDeLista: () -> Unit,
    onPokemonClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    // Se vuelve true cuando el usuario está a pocos elementos del final
    val cercaDelFinal by remember {
        derivedStateOf {
            val ultimoVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            ultimoVisible >= listState.layoutInfo.totalItemsCount - 4
        }
    }

    // Cuando cambia a true y no estamos cargando, pedimos más datos
    LaunchedEffect(cercaDelFinal) {
        if (cercaDelFinal && !cargando) onFinalDeLista()
    }

    // Al cambiar la búsqueda o el filtro, la lista vuelve arriba
    LaunchedEffect(textoBusqueda, tipoSeleccionado) {
        listState.scrollToItem(0)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { IndicadorConexion(hayConexion) }

            item {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = onTextoBusquedaChange,
                    label = { Text("Buscar por nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { FiltroTipos(tipoSeleccionado, onTipoSeleccionado) }

            error?.let { mensaje ->
                item { Text(mensaje, color = Color(0xFFC62828)) }
            }

            items(pokemones, key = { it.id }) { pokemon ->
                TarjetaPokemon(
                    pokemon = pokemon,
                    onClick = { onPokemonClick(pokemon.nombre) }
                )
            }

            if (cargando) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun IndicadorConexion(hayConexion: Boolean) {
    val texto = if (hayConexion) "Online" else "Offline"
    val color = if (hayConexion) Color(0xFF2E7D32) else Color(0xFFC62828)

    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = texto,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun FiltroTipos(seleccionado: Tipo?, onSeleccionar: (Tipo?) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(Tipo.values().toList()) { tipo ->
            FilterChip(
                selected = seleccionado == tipo,
                onClick = { onSeleccionar(if (seleccionado == tipo) null else tipo) },
                label = { Text(tipo.name.lowercase().replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
fun TarjetaPokemon(pokemon: PokemonResumen, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "#${pokemon.id}  ${pokemon.nombre.replaceFirstChar { it.uppercase() }}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}