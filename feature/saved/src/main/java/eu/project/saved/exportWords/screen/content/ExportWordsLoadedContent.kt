package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import eu.project.common.TestTags
import eu.project.saved.exportWords.state.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.state.ExportWordsSubscreen
import eu.project.saved.exportWords.ui.subscreenController

@Composable
internal fun ColumnScope.exportWordsLoadedContent(
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit,
    onSwitchToSelectWords: () -> Unit,
    onTryToSwitchToExportSettings: () -> Unit,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    subscreenController(
        subscreenControllerState = uiState.subscreenControllerState,
        onSwitchToSelectWords = onSwitchToSelectWords,
        onTryToSwitchToExportSettings = onTryToSwitchToExportSettings
    )

    when(uiState.currentSubscreen) {

        ExportWordsSubscreen.SelectWords -> selectWordsContent(
            selectWordsUiState = uiState.selectWordsUiState,
            wordsToExport = uiState.wordsToExport,
            onChangeWordSelection = onChangeWordSelection
        )

        ExportWordsSubscreen.ExportSettings -> exportSettingsContent(
            exportSettingsUiState = uiState.exportSettingsUiState,
            onClickSendMethod = onClickSendMethod,
            onClickDownloadMethod = onClickDownloadMethod,
        )
    }
}