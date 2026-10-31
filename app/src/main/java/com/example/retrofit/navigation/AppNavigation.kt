package com.example.retrofit.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.retrofit.features.detalle.DetalleScreen
import com.example.retrofit.features.detalle.DetalleViewModel
import com.example.retrofit.features.lista.ListaScreen
import com.example.retrofit.features.lista.ListaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Lista
        ) {

            composable<MainRoutes.Lista> {
                val viewModel: ListaViewModel = hiltViewModel()
                val hayConexion by viewModel.hayConexion.collectAsState()

                ListaScreen(
                    pokemones = viewModel.pokemones,
                    textoBusqueda = viewModel.texto,
                    onTextoBusquedaChange = viewModel::cambiarTexto,
                    tipoSeleccionado = viewModel.tipo,
                    onTipoSeleccionado = viewModel::cambiarTipo,
                    hayConexion = hayConexion,
                    cargando = viewModel.cargando,
                    error = viewModel.error,
                    onFinalDeLista = viewModel::cargarSiguientePagina,
                    onPokemonClick = { nombre ->
                        navController.navigate(MainRoutes.Detalle(nombre))
                    }
                )
            }

            composable<MainRoutes.Detalle> {
                // El nombre llega al ViewModel desde los argumentos de la ruta
                val viewModel: DetalleViewModel = hiltViewModel()

                DetalleScreen(
                    pokemon = viewModel.pokemon,
                    error = viewModel.error,
                    onReintentar = viewModel::cargar,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}