package eu.project.saved.exportWords.state

import eu.project.saved.exportWords.model.Email
import eu.project.saved.exportWords.model.ExportMethod
import eu.project.saved.exportWords.model.ExportableSavedWord

internal data class ExportWordsUiState(
    
    // core business data
    val wordsToExport: List<ExportableSavedWord> = listOf(),
    val exportMethod: ExportMethod = ExportMethod.NotSpecified,
    val email: Email? = null,

    // navigation state
    val currentSubscreen: ExportWordsSubscreen = ExportWordsSubscreen.SelectWords,
    val subscreenControllerState: SubscreenControllerState = SubscreenControllerState(),

    // ui state for each subscreen
    val selectWordsUiState: SelectWordsUiState = SelectWordsUiState(),
    val exportSettingsUiState: ExportSettingsUiState = ExportSettingsUiState(),
)