package eu.project.common.connectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityObserver {

    val connectivityStatus: StateFlow<ConnectivityStatus>
}