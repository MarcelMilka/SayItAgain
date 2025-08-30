package eu.project.saved.exportResult.state

import eu.project.common.remoteData.CsvFile

internal sealed class ExportResultScreenState {
    data object Loading: ExportResultScreenState()
    data class ReadyToSaveFile(val csvFile: CsvFile): ExportResultScreenState()
    data class FailedToLoadFile(val error: Throwable): ExportResultScreenState()

    data object SavingFile: ExportResultScreenState()
    data object FileSavedSuccessfully: ExportResultScreenState()
    data class SaveFileError(val error: Throwable): ExportResultScreenState()
}