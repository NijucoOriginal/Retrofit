package com.example.retrofit.features.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.retrofit.domain.model.PokemonResumen
import com.example.retrofit.domain.model.Tipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    estado: ListaUiState,
    textoBusqueda: String,
    onTextoBusquedaChange: (String) -> Unit,
    tipoSeleccionado: Tipo?,
    onTipoSeleccionado: (Tipo?) -> Unit,
    hayConexion: Boolean,
    onFinalDeLista: () -> Unit,
    onReintentar: () -> Unit,
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

    // Solo se pide más si ya hay una lista que ampliar
    LaunchedEffect(cercaDelFinal) {
        if (cercaDelFinal && estado is ListaUiState.Exito) onFinalDeLista()
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

            when (estado) {
                ListaUiState.Cargando -> item { CargaCentrada() }

                is ListaUiState.Error -> item {
                    MensajeConReintento(estado.mensaje, onReintentar)
                }

                is ListaUiState.Exito -> {
                    if (estado.pokemones.isEmpty()) {
                        item {
                            Text(
                                "No hay Pokémon que coincidan con la búsqueda.",
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }

                    items(estado.pokemones, key = { it.id }) { pokemon ->
                        TarjetaPokemon(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.nombre) }
                        )
                    }

                    if (estado.cargandoMas) {
                        item { CargaCentrada() }
                    }

                    estado.errorMas?.let { mensaje ->
                        item { MensajeConReintento(mensaje, onReintentar) }
                    }
                }
            }
        }
    }
}

@Composable
fun CargaCentrada() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MensajeConReintento(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(mensaje, color = Color(0xFFC62828))
        Button(onClick = onReintentar) {
            Text("Reintentar")
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.imagenUrl,
                contentDescription = pokemon.nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "#${pokemon.id}  ${pokemon.nombre.replaceFirstChar { it.uppercase() }}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}