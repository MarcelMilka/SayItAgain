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

    // ui state for each subscreen
//    val selectWordsUiState: SelectWordsUiState = SelectWordsUiState(),
    val exportSettingsUiState: ExportSettingsUiState = ExportSettingsUiState(),

    val subscreenControllerState: SubscreenControllerState = SubscreenControllerState(),
    val showNoWordsSelectedBanner: Boolean = false,
    val exportMethodControllerState: ExportMethodControllerState = ExportMethodControllerState(),
    val showEmailTextField: Boolean = false,
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