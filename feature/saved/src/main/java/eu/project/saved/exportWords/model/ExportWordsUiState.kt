package eu.project.saved.exportWords.model

internal data class ExportWordsUiState(
    val exportableWords: List<ExportableSavedWord> = listOf()
)