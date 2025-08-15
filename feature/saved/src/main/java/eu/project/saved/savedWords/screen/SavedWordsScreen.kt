package eu.project.saved.savedWords.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import eu.project.saved.savedWords.ui.discardWordDialog
import eu.project.saved.savedWords.ui.failedToLoadComponent
import eu.project.saved.savedWords.ui.loadedDataComponent
import eu.project.saved.savedWords.ui.loadingComponent
import eu.project.saved.savedWords.ui.noDataComponent
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun savedWordsScreen(
    screenState: SavedWordsScreenState,
    dialogState: DiscardWordDialogState,
    onRequestDelete: (SavedWord) -> Unit,
    onDelete: (SavedWord) -> Unit,
    onCancel: () -> Unit,
    onNavigateSelectAudioScreen: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(tag = TestTags.SAVED_WORDS_SCREEN),
        content = {

            when(screenState) {

                SavedWordsScreenState.Loading ->
                    loadingComponent()

                SavedWordsScreenState.Loaded.NoData ->
                    noDataComponent { onNavigateSelectAudioScreen() }

                is SavedWordsScreenState.Loaded.Data ->
                    loadedDataComponent(
                        retrievedData = screenState.retrievedData,
                        onRequestDelete = { onRequestDelete(it) }
                    )

                is SavedWordsScreenState.Error ->
                    failedToLoadComponent(
                        cause = screenState.cause
                    )
            }
        }
    )

    if (dialogState is DiscardWordDialogState.Visible) {

        discardWordDialog(
            wordToDelete = dialogState.wordToDelete,
            onDelete = { onDelete(dialogState.wordToDelete) },
            onCancel = { onCancel() }
        )
    }
}