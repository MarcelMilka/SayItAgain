package eu.project.saved.exportWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.saved.exportWords.model.ExportWordsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExportWordsViewModel @Inject constructor(
    private val savedWordsRepository: SavedWordsRepository,
    private val connectivityObserver: ConnectivityObserver,
): ViewModel() {


    private var _screenState = MutableStateFlow<ExportWordsScreenState>(ExportWordsScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {

        viewModelScope.launch {

            observeExternalFactors()
        }
    }



    // screen state
    private suspend fun observeExternalFactors() {

        savedWordsRepository.dataState
            .combine(connectivityObserver.connectivityStatus) { dataState, connectivityStatus ->

                CombinedFlows(dataState, connectivityStatus)
            }
            .map { combinedFlows ->

                evaluateScreenState(combinedFlows)
            }
            .distinctUntilChanged()
            .collect { screenState ->

                _screenState.value = screenState
            }
    }

    private fun evaluateScreenState(combinedFlows: CombinedFlows): ExportWordsScreenState {

        return when(combinedFlows.dataState) {

            is SavedWordsRepositoryDataState.FailedToLoad -> { ExportWordsScreenState.Error }
            SavedWordsRepositoryDataState.Loaded.NoData -> { ExportWordsScreenState.Error }
            SavedWordsRepositoryDataState.Loading -> { ExportWordsScreenState.Error }

            is SavedWordsRepositoryDataState.Loaded.Data -> {

                when(combinedFlows.connectivityStatus) {

                    ConnectivityStatus.Connected -> ExportWordsScreenState.ReadyToExport
                    ConnectivityStatus.Disconnected -> ExportWordsScreenState.Disconnected
                }
            }
        }
    }

    private data class CombinedFlows(
        val dataState: SavedWordsRepositoryDataState,
        val connectivityStatus: ConnectivityStatus
    )
}