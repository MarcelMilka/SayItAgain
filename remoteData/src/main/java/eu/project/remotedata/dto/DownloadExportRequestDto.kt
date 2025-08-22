package eu.project.remotedata.dto

import com.fasterxml.jackson.annotation.JsonProperty
import eu.project.common.model.SavedWord

internal data class DownloadExportRequestDto(
    @JsonProperty("wordsToExport") val wordsToExport: List<WordDto>
)

internal data class WordDto(
    @JsonProperty("value") val value: String
)

internal fun List<SavedWord>.convertToDto(): DownloadExportRequestDto {

    val wordsToExport = this.map { WordDto(value = it.word) }

    return DownloadExportRequestDto(
        wordsToExport = wordsToExport
    )
}