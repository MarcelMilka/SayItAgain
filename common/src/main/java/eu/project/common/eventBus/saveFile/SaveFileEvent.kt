package eu.project.common.eventBus.saveFile

import eu.project.common.remoteData.CsvFile

sealed class SaveFileEvent {

    data object Idle : SaveFileEvent()
    data class SaveFile(val csvFile: CsvFile) : SaveFileEvent()
    data object FileSavedSuccessfully : SaveFileEvent()
    data class SaveFileError(val error: Throwable) : SaveFileEvent()
}