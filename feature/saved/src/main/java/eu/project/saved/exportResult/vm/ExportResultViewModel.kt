package eu.project.saved.exportResult.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.eventBus.saveFile.SaveFileEvent
import eu.project.common.eventBus.saveFile.SaveFileEventBus
import eu.project.common.model.ExportSettings
import eu.project.common.model.decodeToExportSettings
import eu.project.common.remoteData.ExportRepository
import eu.project.saved.exportResult.state.ExportResultScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExportResultViewModel @Inject constructor(
    val exportRepository: ExportRepository,
    val saveFileEventBus: SaveFileEventBus
): ViewModel() {

    private var _exportSettings = MutableSharedFlow<ExportSettings>(replay = 1)

    private var _screenState = MutableStateFlow<ExportResultScreenState>(ExportResultScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {

        tryToDownloadCsv()
        evaluateScreenState()
    }

    private fun tryToDownloadCsv() {

        viewModelScope.launch {

            val exportSettings = _exportSettings.first()

            val requestDownloadToDevice = exportRepository
                    .requestDownloadToDevice(wordsToExport = exportSettings.wordsToExport)

            requestDownloadToDevice.fold(
                onSuccess = { csvFile ->

                    _screenState.update { ExportResultScreenState.ReadyToSaveFile(csvFile = csvFile) }
                },
                onFailure = { throwable ->

                    _screenState.update { ExportResultScreenState.FailedToLoadFile(error = throwable) }
                }
            )
        }
    }

    private fun evaluateScreenState() {

        viewModelScope.launch {

            saveFileEventBus
                .events
                .distinctUntilChanged()
                .collect { event ->

                    val newScreenState = when (event) {
                        SaveFileEvent.Idle -> ExportResultScreenState.Loading
                        is SaveFileEvent.SaveFile -> ExportResultScreenState.SavingFile
                        SaveFileEvent.FileSavedSuccessfully -> ExportResultScreenState.FileSavedSuccessfully
                        is SaveFileEvent.SaveFileError -> ExportResultScreenState.SaveFileError(error = event.error)
                    }

                    _screenState.update { newScreenState }
                }
        }
    }



    fun retrieveExportSettings(exportSettingsSerialized: String) {

        viewModelScope.launch {

            try {

                val exportSettings = exportSettingsSerialized.decodeToExportSettings()
                _exportSettings.emit(exportSettings)
            }

            catch (e: Exception) {

                _screenState.update { ExportResultScreenState.FailedToLoadFile(error = e) }
            }
        }
    }
}