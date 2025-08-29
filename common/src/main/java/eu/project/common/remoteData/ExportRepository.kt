package eu.project.common.remoteData

import eu.project.common.model.Word

interface ExportRepository {

    suspend fun requestDownloadToDevice(wordsToExport: List<Word>): Result<CsvFile>
}