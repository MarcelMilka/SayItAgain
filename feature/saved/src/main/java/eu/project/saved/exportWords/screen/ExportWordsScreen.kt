package eu.project.saved.exportWords.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.state.ExportWordsScreenState
import eu.project.saved.exportWords.state.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.screen.content.exportWordsLoadedContent
import eu.project.saved.exportWords.screen.content.exportWordsDisconnectedContent
import eu.project.saved.exportWords.screen.content.exportWordsErrorContent
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun exportWordsScreen(
    screenState: ExportWordsScreenState,
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit,
    onSwitchToSelectWords: () -> Unit,
    onTryToSwitchToExportSettings: () -> Unit,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(TestTags.EXPORT_WORDS_SCREEN),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        content = {

            when(screenState) {
                ExportWordsScreenState.Error -> exportWordsErrorContent()
                ExportWordsScreenState.Loading -> exportWordsErrorContent()
                ExportWordsScreenState.Disconnected -> exportWordsDisconnectedContent()
                ExportWordsScreenState.Loaded -> exportWordsLoadedContent(
                    uiState = uiState,
                    onChangeWordSelection = onChangeWordSelection,
                    onSwitchToSelectWords = onSwitchToSelectWords,
                    onTryToSwitchToExportSettings = onTryToSwitchToExportSettings,
                    onClickSendMethod = onClickSendMethod,
                    onClickDownloadMethod = onClickDownloadMethod
                )
            }
        }
    )
}