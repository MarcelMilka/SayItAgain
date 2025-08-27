package eu.project.saved.exportWords.model

import eu.project.common.model.SavedWord
import eu.project.common.model.Word
import java.util.UUID

internal data class ExportableSavedWord(
    val uuid: UUID,
    val word: String,
    val language: String,
    val toExport: Boolean = false
)

internal fun ExportableSavedWord.convertToModel(): SavedWord =
    SavedWord(
        uuid = this.uuid,
        word = this.word,
        language = this.language
    )

internal fun SavedWord.convertToExportable(): ExportableSavedWord =
    ExportableSavedWord(
        uuid = this.uuid,
        word = this.word,
        language = this.language
    )

internal fun ExportableSavedWord.convertToWord(): Word =
    Word(value = this.word)