package eu.project.saved.exportWords.intent

import eu.project.saved.exportWords.model.ExportableSavedWord

internal sealed interface ExportWordsIntent {

    data class ChangeWordSelection(val wordToUpdate: ExportableSavedWord): ExportWordsIntent
    object SwitchToSelectWords: ExportWordsIntent
    object SwitchToExportSettings: ExportWordsIntent
}