package eu.project.common.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val connectivityStatus: Flow<ConnectivityStatus>
}