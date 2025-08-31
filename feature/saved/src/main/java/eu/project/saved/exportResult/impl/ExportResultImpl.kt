package eu.project.saved.exportResult.impl

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import eu.project.common.navigation.Navigation
import eu.project.saved.exportResult.screen.exportResultScreen
import eu.project.saved.exportResult.vm.ExportResultViewModel

fun NavGraphBuilder.exportResultImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportResultScreen> { backStackEntry ->

        val viewModel = hiltViewModel<ExportResultViewModel>()
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        exportResultScreen(
            screenState = screenState,
            onClickSaveExportedWords = {},
            onClickTryAgainLater = {}
        )

        val exportSettingsSerialized = remember {
            backStackEntry
                .toRoute<Navigation.Saved.ExportResultScreen>()
                .exportSettingsSerialized
        }

        LaunchedEffect(exportSettingsSerialized) {

            viewModel.retrieveExportSettings(exportSettingsSerialized = exportSettingsSerialized)
        }
    }
}