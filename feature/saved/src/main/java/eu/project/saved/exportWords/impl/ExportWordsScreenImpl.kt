package eu.project.saved.exportWords.impl

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.common.SavedWordsSharedViewModel
import eu.project.saved.common.sharedViewModel
import eu.project.saved.exportWords.screen.exportWordsScreen
import eu.project.saved.exportWords.vm.ExportWordsViewModel

fun NavGraphBuilder.exportWordsImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportWordsScreen> { backStackEntry ->

        val sharedViewModel = backStackEntry.sharedViewModel<SavedWordsSharedViewModel>(controller)
        val listState = sharedViewModel.listState

        val viewModel = hiltViewModel<ExportWordsViewModel>()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        exportWordsScreen()
    }
}