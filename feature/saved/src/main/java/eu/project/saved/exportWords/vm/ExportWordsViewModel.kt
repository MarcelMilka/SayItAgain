package eu.project.saved.exportWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.saved.exportWords.intent.ExportWordsIntent
import eu.project.saved.exportWords.model.ExportMethod
import eu.project.saved.exportWords.model.ExportMethodVariants
import eu.project.saved.exportWords.state.ExportWordsScreenState
import eu.project.saved.exportWords.state.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.model.convertToExportable
import eu.project.saved.exportWords.model.convertToModel
import eu.project.saved.exportWords.state.ExportWordsSubscreen
import eu.project.saved.exportWords.state.SubscreenControllerButtonVariants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExportWordsViewModel @Inject constructor(
    private val savedWordsRepository: SavedWordsRepository,
    private val connectivityObserver: ConnectivityObserver,
): ViewModel() {


    private var _screenState = MutableStateFlow<ExportWordsScreenState>(ExportWordsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private var _uiState = MutableStateFlow<ExportWordsUiState>(ExportWordsUiState())
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {

            observeExternalFactors()
        }
    }



    private suspend fun observeExternalFactors() {

        savedWordsRepository.dataState
            .combine(connectivityObserver.connectivityStatus) { dataState, connectivityStatus ->

                CombinedFlows(dataState, connectivityStatus)
            }
            .distinctUntilChanged()
            .onEach { combinedFlows ->

                evaluateScreenState(combinedFlows)
                evaluateUiStateIfNeeded(combinedFlows)
            }
            .collect()
    }

    private fun evaluateScreenState(combinedFlows: CombinedFlows) {

        val newScreenState = when(combinedFlows.dataState) {

            is SavedWordsRepositoryDataState.Loaded.Data -> {

                when(combinedFlows.connectivityStatus) {

                    ConnectivityStatus.Connected -> ExportWordsScreenState.Loaded
                    ConnectivityStatus.Disconnected -> ExportWordsScreenState.Disconnected
                }
            }

            else -> ExportWordsScreenState.Error
        }

        _screenState.update { newScreenState }
    }

    private fun evaluateUiStateIfNeeded(combinedFlows: CombinedFlows) {

        val currentModelList = _uiState.value.wordsToExport.map { it.convertToModel() }

        val newModelList = when (val dataState = combinedFlows.dataState) {

            is SavedWordsRepositoryDataState.Loaded.Data -> dataState.retrievedData
            else -> null
        }

        if (newModelList != null && newModelList != currentModelList) {

            val newExportableList = newModelList.map { it.convertToExportable() }
            _uiState.update { it.copy(wordsToExport = newExportableList) }
        }
    }

    private data class CombinedFlows(
        val dataState: SavedWordsRepositoryDataState,
        val connectivityStatus: ConnectivityStatus
    )



    fun onIntent(intent: ExportWordsIntent) {

        when (intent) {
            is ExportWordsIntent.ChangeWordSelection -> changeWordSelection(intent.wordToUpdate)
            ExportWordsIntent.TryToSwitchToExportSettings -> tryToSwitchToExportSettings()
            ExportWordsIntent.SwitchToSelectWords -> switchToSelectWords()
            ExportWordsIntent.SelectExportMethodSend -> selectExportMethodSend()
            ExportWordsIntent.SelectExportMethodDownload -> selectExportMethodDownload()
        }
    }

    private fun changeWordSelection(wordToUpdate: ExportableSavedWord) {

        _uiState.update { currentState ->

            val updatedList = currentState.wordsToExport.map { word ->

                if (word.uuid == wordToUpdate.uuid) word.copy(toExport = !word.toExport)
                else word
            }

            val showNoWordsSelectedBanner =
                if (currentState.selectWordsUiState.showNoWordsSelectedBanner && updatedList.any { it.toExport == true }) { false }
                else { currentState.selectWordsUiState.showNoWordsSelectedBanner }

            currentState.copy(
                wordsToExport = updatedList,
                selectWordsUiState = currentState.selectWordsUiState.copy(
                    showNoWordsSelectedBanner = showNoWordsSelectedBanner
                )
            )
        }
    }

    private fun tryToSwitchToExportSettings() {

        val atLeastOneSelectedWord = _uiState.value.wordsToExport.any { it.toExport }

        if (atLeastOneSelectedWord) {

            _uiState.update { uiState ->

                uiState.copy(
                    currentSubscreen = ExportWordsSubscreen.ExportSettings,

                    subscreenControllerState = uiState.subscreenControllerState.copy(
                        selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsEnabled,
                        exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsDisabled
                    )
                )
            }
        }

        else {

            if (!_uiState.value.selectWordsUiState.showNoWordsSelectedBanner) {

                _uiState.update { uiState ->

                    uiState.copy(selectWordsUiState = uiState.selectWordsUiState.copy(showNoWordsSelectedBanner = true))
                }
            }
        }
    }

    private fun switchToSelectWords() {

        _uiState.update { uiState ->

            uiState.copy(
                currentSubscreen = ExportWordsSubscreen.SelectWords,

                subscreenControllerState = uiState.subscreenControllerState.copy(
                    selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsDisabled,
                    exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsEnabled
                )
            )
        }
    }

    private fun selectExportMethodSend() {

        _uiState.update { uiState ->
            
            uiState.copy(
                exportMethod = ExportMethod.SendToEmail,

                exportSettingsUiState = uiState.exportSettingsUiState.copy(
                    sendMethodState = ExportMethodVariants.sendSelected,
                    downloadMethodState = ExportMethodVariants.downloadNotSelected,
                    showExportMethodNotAvailableBanner = true,
//                    emailTextFieldUiState = uiState.exportSettingsUiState.emailTextFieldUiState.copy(
//                        isVisible = true
//                    )
                )
            )
        }
    }

    private fun selectExportMethodDownload() {

        _uiState.update { uiState ->

            uiState.copy(
                exportMethod = ExportMethod.DownloadToDevice,

                exportSettingsUiState = uiState.exportSettingsUiState.copy(
                    sendMethodState = ExportMethodVariants.sendNotSelected,
                    downloadMethodState = ExportMethodVariants.downloadSelected,
                    showExportMethodNotAvailableBanner = false
//                    emailTextFieldUiState = uiState.exportSettingsUiState.emailTextFieldUiState.copy(
//                        isVisible = false
//                    )
                )
            )
        }
    }
}