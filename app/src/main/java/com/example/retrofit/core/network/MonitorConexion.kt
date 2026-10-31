package com.example.retrofit.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonitorConexion @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    private val _hayConexion = MutableStateFlow(hayInternetActivo())
    val hayConexion: StateFlow<Boolean> = _hayConexion.asStateFlow()

    // Se avisa cada vez que cambia la red predeterminada o sus capacidades
    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            _hayConexion.value = capabilities.hasCapability(NET_CAPABILITY_INTERNET)
        }

        override fun onLost(network: Network) {
            _hayConexion.value = hayInternetActivo()
        }
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    private fun hayInternetActivo(): Boolean {
        val caps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return caps?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }
}