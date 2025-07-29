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
import eu.project.saved.savedWords.model.DialogViewState
import eu.project.saved.savedWords.model.SavedWordsScreenViewState
import eu.project.saved.savedWords.ui.discardWordDialog
import eu.project.saved.savedWords.ui.failedToLoadComponent
import eu.project.saved.savedWords.ui.loadedDataComponent
import eu.project.saved.savedWords.ui.loadingComponent
import eu.project.saved.savedWords.ui.noDataComponent
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun savedWordsScreen(
    viewState: SavedWordsScreenViewState,
    dialogViewState: DialogViewState,

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

            when(viewState) {

                SavedWordsScreenViewState.Loading ->
                    loadingComponent()

                SavedWordsScreenViewState.Loaded.NoData ->
                    noDataComponent { onNavigateSelectAudioScreen() }

                is SavedWordsScreenViewState.Loaded.Data ->
                    loadedDataComponent(
                        retrievedData = viewState.retrievedData,
                        onRequestDelete = { onRequestDelete(it) }
                    )

                is SavedWordsScreenViewState.FailedToLoad ->
                    failedToLoadComponent(
                        cause = viewState.cause
                    )
            }
        }
    )

    if (dialogViewState is DialogViewState.Visible) {

        discardWordDialog(
            wordToDelete = dialogViewState.wordToDelete,
            onDelete = { onDelete(dialogViewState.wordToDelete) },
            onCancel = { onCancel() }
        )
    }
}