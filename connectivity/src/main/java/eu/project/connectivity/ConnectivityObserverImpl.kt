package eu.project.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal class ConnectivityObserverImpl @Inject constructor(
    private val applicationContext: Context
): ConnectivityObserver {

    private var _connectivityStatus = MutableStateFlow<ConnectivityStatus>(ConnectivityStatus.Disconnected)
    override val connectivityStatus: StateFlow<ConnectivityStatus> = _connectivityStatus

    //  NetworkRequest - describes app's connection requirements:
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    //  NetworkCallback - receives notifications about changes in the connection status and network capabilities:
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            val reachableByInternet = networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )

            when(reachableByInternet) {
                true -> updateConnectivityStatus(ConnectivityStatus.Connected)
                false -> updateConnectivityStatus(ConnectivityStatus.Disconnected)
            }
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            updateConnectivityStatus(ConnectivityStatus.Connected)
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            updateConnectivityStatus(ConnectivityStatus.Disconnected)
        }

        override fun onUnavailable() {
            super.onUnavailable()

            updateConnectivityStatus(ConnectivityStatus.Disconnected)
        }
    }

    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //  "Listen" to changes with registerNetworkCallback
    init {

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun updateConnectivityStatus(connectivityStatus: ConnectivityStatus) {

        _connectivityStatus.value = connectivityStatus
    }
}