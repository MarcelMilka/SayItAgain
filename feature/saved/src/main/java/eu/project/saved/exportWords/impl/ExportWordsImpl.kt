package eu.project.saved.exportWords.impl

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.saved.exportWords.intent.ExportWordsIntent
import eu.project.saved.exportWords.screen.exportWordsScreen
import eu.project.saved.exportWords.vm.ExportWordsViewModel

fun NavGraphBuilder.exportWordsImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportWordsScreen> { backStackEntry ->

        val viewModel = hiltViewModel<ExportWordsViewModel>()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        exportWordsScreen(
            screenState = screenState,
            uiState = uiState,
            onChangeWordSelection = { viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(it)) },
            onSwitchToSelectWords = { viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords) },
            onTryToSwitchToExportSettings = { viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings) },
            onClickSendMethod = { viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend) },
            onClickDownloadMethod = { viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload) },
        )
    }
}