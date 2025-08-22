package eu.project.common.remoteData

import eu.project.common.model.SavedWord

interface ExportRepository {

    suspend fun requestDownloadToDevice(wordsToExport: List<SavedWord>): Result<CsvFile>
}