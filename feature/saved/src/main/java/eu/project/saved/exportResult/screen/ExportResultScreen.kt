package eu.project.saved.exportResult.screen

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
import eu.project.saved.exportResult.screen.content.exportResultFailedToLoadFileContent
import eu.project.saved.exportResult.screen.content.exportResultLoadingContent
import eu.project.saved.exportResult.screen.content.exportResultReadyToSaveFileContent
import eu.project.saved.exportResult.state.ExportResultScreenState
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun exportResultScreen(
    screenState: ExportResultScreenState,
    onClickSaveExportedWords: () -> Unit,
    onClickTryAgainLater: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(TestTags.EXPORT_RESULT_SCREEN),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            when(screenState) {
                ExportResultScreenState.Loading -> exportResultLoadingContent()
                is ExportResultScreenState.ReadyToSaveFile -> exportResultReadyToSaveFileContent { onClickSaveExportedWords() }
                is ExportResultScreenState.FailedToLoadFile -> exportResultFailedToLoadFileContent(
                    throwable = screenState.error,
                    onClickTryAgainLater = onClickTryAgainLater
                )

                ExportResultScreenState.SavingFile -> {}
                ExportResultScreenState.FileSavedSuccessfully -> {}
                is ExportResultScreenState.SaveFileError -> {}
            }
        }
    )
}