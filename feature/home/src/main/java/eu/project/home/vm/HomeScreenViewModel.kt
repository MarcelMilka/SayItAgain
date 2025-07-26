package eu.project.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeScreenViewModel @Inject constructor(connectivityObserver: ConnectivityObserver): ViewModel() {

    val isNetworkAvailable =
        connectivityObserver
        .connectivityStatus
        .distinctUntilChanged()
        .map { connectivityStatus ->

            when (connectivityStatus) {
                ConnectivityStatus.Connected -> true
                ConnectivityStatus.Disconnected -> false
            }
        }
        .stateIn(
            scope = this.viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = true
        )
}