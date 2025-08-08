package eu.project.saved.exportWords.screen.subscreen

import eu.project.saved.exportWords.ui.exportMethodSelector
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import eu.project.saved.exportWords.model.ExportWordsUiState

@Composable
internal fun ColumnScope.exportSettingsSubscreen(
    uiState: ExportWordsUiState,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    exportMethodSelector(
        uiState = uiState,
        onClickSendMethod = { onClickSendMethod() },
        onClickDownloadMethod = { onClickDownloadMethod() }
    )
}