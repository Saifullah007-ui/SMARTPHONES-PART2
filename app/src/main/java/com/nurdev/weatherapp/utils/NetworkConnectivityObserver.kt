package com.nurdev.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest


class NetworkConnectivityObserver {
    private var networkCallback: NetworkCallback? = null

    interface ConnectivityChangeListener {
        fun onNetworkChanged(isConnected: Boolean)
    }

    fun observeConnectivityChanges(context: Context, listener: ConnectivityChangeListener) {
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.onNetworkChanged(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.onNetworkChanged(false)
            }
        }
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest,
            networkCallback as NetworkCallback
        )
    }

    fun stopObservingConnectivityChanges(context: Context) {
        if (networkCallback != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback!!)
            networkCallback = null
        }
    }
}