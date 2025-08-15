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
import eu.project.saved.savedWords.screen.content.savedWordsErrorContent
import eu.project.saved.savedWords.screen.content.savedWordsDataContent
import eu.project.saved.savedWords.screen.content.savedWordsLoadingContent
import eu.project.saved.savedWords.screen.content.savedWordsNoDataContent
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

                SavedWordsScreenState.Loading -> savedWordsLoadingContent()
                is SavedWordsScreenState.Error -> savedWordsErrorContent(cause = screenState.cause)
                SavedWordsScreenState.Loaded.NoData -> savedWordsNoDataContent { onNavigateSelectAudioScreen() }
                is SavedWordsScreenState.Loaded.Data -> savedWordsDataContent(
                    retrievedData = screenState.retrievedData,
                    onRequestDelete = { onRequestDelete(it) }
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