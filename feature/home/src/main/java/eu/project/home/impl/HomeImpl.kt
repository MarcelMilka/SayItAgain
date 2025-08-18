package eu.project.home.impl

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.home.vm.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import eu.project.home.screen.homeScreen


fun NavGraphBuilder.homeImpl(controller: NavHostController) {

    composable<Navigation.HomeScreen> {

        val viewModel = hiltViewModel<HomeViewModel>()
        val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

        homeScreen(
            isNetworkAvailable = isNetworkAvailable,
            onNavigateSelectAudioScreen = {

                controller.navigate(route = Navigation.Transcribe.RouteTranscribe)
            },
            onNavigateSavedWordsScreen = {

                controller.navigate(route = Navigation.Saved.RouteSaved)
            }
        )
    }
}