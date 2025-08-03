package eu.project.saved.savedWords.impl

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.common.SavedWordsSharedViewModel
import eu.project.saved.common.savedWordsSharedViewModel
import eu.project.saved.savedWords.screen.savedWordsScreen
import eu.project.saved.savedWords.vm.SavedWordsScreenViewModel

fun NavGraphBuilder.savedWordsScreenImpl(controller: NavHostController) {

    composable<Navigation.Saved.SavedWordsScreen> {

        val viewModel = hiltViewModel<SavedWordsScreenViewModel>()
        val viewState by viewModel.viewState.collectAsStateWithLifecycle()
        val dialogViewState by viewModel.dialogViewState.collectAsStateWithLifecycle()

        val sharedViewModel = controller.savedWordsSharedViewModel<SavedWordsSharedViewModel>()
        val listState = sharedViewModel.listState

        savedWordsScreen(
            viewState = viewState,
            listState = listState,
            dialogViewState = dialogViewState,

            onRequestDelete = { viewModel.requestWordDeletion(it) },
            onDelete = { viewModel.deleteWord(it) },
            onCancel = { viewModel.cancelWordDeletion() },
            onNavigateSelectAudioScreen = { controller.navigate(Navigation.Saved.ExportWordsScreen) }
        )
    }
}