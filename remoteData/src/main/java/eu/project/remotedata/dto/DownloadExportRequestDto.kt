package eu.project.remotedata.dto

import com.google.gson.annotations.SerializedName
import eu.project.common.model.SavedWord

internal data class DownloadExportRequestDto(
    @SerializedName("wordsToExport") val wordsToExport: List<WordDto>
)

internal data class WordDto(
    @SerializedName("value") val value: String
)

internal fun List<SavedWord>.convertToDto(): DownloadExportRequestDto {

    val wordsToExport = this.map { WordDto(value = it.word) }

    return DownloadExportRequestDto(
        wordsToExport = wordsToExport
    )
}