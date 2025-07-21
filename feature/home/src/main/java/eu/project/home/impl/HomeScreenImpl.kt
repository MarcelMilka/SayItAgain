package eu.project.home.impl

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.home.vm.HomeScreenViewModel
import androidx.compose.runtime.getValue


fun NavGraphBuilder.homeScreenImpl() {

    composable<Navigation.HomeScreen> {

        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()
    }
}