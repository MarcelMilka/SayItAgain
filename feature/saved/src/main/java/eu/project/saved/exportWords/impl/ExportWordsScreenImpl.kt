package eu.project.saved.exportWords.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.common.SavedWordsSharedViewModel
import eu.project.saved.common.savedWordsSharedViewModel
import eu.project.saved.exportWords.screen.exportWordsScreen

fun NavGraphBuilder.exportWordsScreenImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportWordsScreen> {

        val sharedViewModel = controller.savedWordsSharedViewModel<SavedWordsSharedViewModel>()
        val listState = sharedViewModel.listState

        exportWordsScreen()
    }
}