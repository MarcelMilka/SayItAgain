package eu.project.topBar.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import eu.project.topBar.screen.topBar
import eu.project.topBar.vm.TopBarViewModel

@Composable
internal fun topBarImpl(controller: NavHostController) {

    val viewModel = hiltViewModel<TopBarViewModel>()
    val topBarViewState by viewModel.uiState.collectAsStateWithLifecycle()

    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringAfterLast(".")

    LaunchedEffect(currentRoute) {

        viewModel.onRouteChanged(route = currentRoute)
    }

    topBar(
        topBarViewState = topBarViewState,
        onNavigateBack = { controller.navigateUp() },
        onDisplayInfo = {}
    )
}