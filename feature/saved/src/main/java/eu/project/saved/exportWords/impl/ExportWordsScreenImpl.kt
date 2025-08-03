package eu.project.saved.exportWords.impl

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.exportWords.screen.exportWordsScreen
import eu.project.saved.exportWords.vm.ExportWordsScreenViewModel

fun NavGraphBuilder.exportWordsScreenImpl() {

    composable<Navigation.Saved.ExportWordsScreen> {

        val viewModel = hiltViewModel<ExportWordsScreenViewModel>()

        exportWordsScreen()
    }
}