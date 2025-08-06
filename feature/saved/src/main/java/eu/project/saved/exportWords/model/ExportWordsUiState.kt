package eu.project.saved.exportWords.model

import eu.project.saved.exportWords.ui.SubscreenControllerState

internal data class ExportWordsUiState(
    val exportableWords: List<ExportableSavedWord> = listOf(),
    val subscreenControllerState: SubscreenControllerState = SubscreenControllerState()
)