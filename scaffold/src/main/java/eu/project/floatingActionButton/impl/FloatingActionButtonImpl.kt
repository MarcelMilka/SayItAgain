package eu.project.floatingActionButton.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import eu.project.floatingActionButton.vm.FloatingActionButtonViewModel

@Composable
internal fun floatingActionButtonImpl(controller: NavHostController) {

    val viewModel = hiltViewModel<FloatingActionButtonViewModel>()

    val visibilityState by viewModel.visibilityState.collectAsStateWithLifecycle()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringAfterLast(".")

    LaunchedEffect(currentRoute) {

        viewModel.onRouteChanged(route = currentRoute)
    }
}