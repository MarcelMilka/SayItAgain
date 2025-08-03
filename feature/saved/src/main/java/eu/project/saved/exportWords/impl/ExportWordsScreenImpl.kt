package eu.project.saved.exportWords.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.common.SavedWordsSharedViewModel
import eu.project.saved.common.sharedViewModel
import eu.project.saved.exportWords.screen.exportWordsScreen

fun NavGraphBuilder.exportWordsScreenImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportWordsScreen> { backStackEntry ->

        val sharedViewModel = backStackEntry.sharedViewModel<SavedWordsSharedViewModel>(controller)
        val listState = sharedViewModel.listState

        exportWordsScreen()
    }
}