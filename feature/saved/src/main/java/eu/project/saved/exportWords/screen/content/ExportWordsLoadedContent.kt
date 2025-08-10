package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
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
internal fun BoxScope.exportWordsLoadedContent(
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.EXPORT_WORDS_SCREEN_SHOW_READY_TO_EXPORT),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        content = {

            subscreenController(
                subscreenControllerState = uiState.subscreenControllerState,
                onClickLeft = onClickLeft,
                onClickRight = onClickRight
            )

            when(uiState.subscreenControllerState.exportWordsSubscreen) {

                ExportWordsSubscreen.SelectWords -> selectWordsContent(
                    uiState = uiState,
                    onChangeWordSelection = onChangeWordSelection
                )

                ExportWordsSubscreen.ExportSettings -> exportSettingsContent(
                    exportSettingsUiState = uiState.exportSettingsUiState,
                    onClickSendMethod = onClickSendMethod,
                    onClickDownloadMethod = onClickDownloadMethod,
                )
            }
        }
    )
}