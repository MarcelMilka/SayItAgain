package eu.project.saved.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

@Composable
internal inline fun <reified T : ViewModel> NavHostController.savedWordsSharedViewModel(): T {
    val navGraphRoute = this.currentDestination?.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(navGraphRoute) {
        this.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}