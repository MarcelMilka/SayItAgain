package eu.project.saved.exportWords.state

import eu.project.saved.exportWords.model.Email
import eu.project.saved.exportWords.model.ExportMethod
import eu.project.saved.exportWords.model.ExportMethodVariants
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
    val exportSettingsUiState: ExportSettingsUiState = ExportSettingsUiState(),

    val showNoWordsSelectedBanner: Boolean = false, // TODO: remove it
    val exportMethodControllerState: ExportMethodControllerState = ExportMethodControllerState(), // TODO: remove it
    val showEmailTextField: Boolean = false, // TODO: remove it
)



// ExportSettingsUiState.kt--------------------------------------------------------------------
internal data class ExportSettingsUiState(
    val sendMethodState: ExportMethodState = ExportMethodVariants.sendNotSelected,
    val emailTextFieldUiState: EmailTextFieldUiState = EmailTextFieldUiState(),
    val downloadMethodState: ExportMethodState = ExportMethodVariants.downloadNotSelected,
)

// EmailTextFieldUiState.kt--------------------------------------------------------------------
internal data class EmailTextFieldUiState(
    val isVisible: Boolean = false
)