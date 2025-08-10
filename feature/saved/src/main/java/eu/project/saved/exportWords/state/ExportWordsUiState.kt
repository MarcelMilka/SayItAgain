package eu.project.saved.exportWords.state

import eu.project.saved.exportWords.model.ExportableSavedWord

internal data class ExportWordsUiState(
    
    // core business data
    val wordsToExport: List<ExportableSavedWord> = listOf(),
    
    val subscreenControllerState: SubscreenControllerState = SubscreenControllerState(),
    val showNoWordsSelectedBanner: Boolean = false,
    val exportMethodControllerState: ExportMethodControllerState = ExportMethodControllerState(),
    val showEmailTextField: Boolean = false,
)