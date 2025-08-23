package eu.project.saved.exportWords.state

import eu.project.saved.exportWords.model.ExportMethodVariants

internal data class ExportSettingsUiState(
    val sendMethodState: ExportMethodState = ExportMethodVariants.sendNotSelected,
    val emailTextFieldUiState: EmailTextFieldUiState = EmailTextFieldUiState(),
    val downloadMethodState: ExportMethodState = ExportMethodVariants.downloadNotSelected,

    // temporary properties
    val showExportMethodNotAvailableBanner: Boolean = false,
    val exportWordsButtonState: ExportWordsButtonState = ExportWordsButtonVariants.disabled
)