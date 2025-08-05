package eu.project.saved.exportWords.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsScreenState
import eu.project.saved.exportWords.ui.showReadyToExport
import eu.project.saved.exportWords.ui.showDisconnected
import eu.project.saved.exportWords.ui.showError
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun exportWordsScreen(
    screenState: ExportWordsScreenState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(tag = TestTags.EXPORT_WORDS_SCREEN),
        content = {

            when(screenState) {
                ExportWordsScreenState.Error -> showError()
                ExportWordsScreenState.Initial -> showError()
                ExportWordsScreenState.Disconnected -> showDisconnected()
                ExportWordsScreenState.ReadyToExport -> showReadyToExport()
            }
        }
    )
}