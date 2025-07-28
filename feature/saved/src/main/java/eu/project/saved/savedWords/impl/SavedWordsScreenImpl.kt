package eu.project.saved.savedWords.impl

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.savedWords.model.DialogViewState
import eu.project.saved.savedWords.screen.savedWordsScreen
import eu.project.saved.savedWords.ui.discardWordDialog
import eu.project.saved.savedWords.vm.SavedWordsScreenViewModel

fun NavGraphBuilder.savedWordsScreenImpl() {

    composable<Navigation.Saved.SavedWordsScreen> {

        val viewModel = hiltViewModel<SavedWordsScreenViewModel>()
        val viewState by viewModel.viewState.collectAsStateWithLifecycle()
        val dialogViewState by viewModel.dialogViewState.collectAsStateWithLifecycle()

        savedWordsScreen(
            viewState = viewState,
            onRequestDelete = { viewModel.requestWordDeletion(it) },
            onNavigateSelectAudioScreen = {}
        )

        if (dialogViewState is DialogViewState.Visible) {

            discardWordDialog(
                wordToDelete = (dialogViewState as DialogViewState.Visible).wordToDelete,
                onDelete = { viewModel.deleteWord(it) },
                onCancel = { viewModel.cancelWordDeletion() }
            )
        }
    }
}