package eu.project.saved.exportResult.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation

fun NavGraphBuilder.exportResultImpl(controller: NavHostController) {

    composable<Navigation.Saved.ExportResultScreen> { backStackEntry -> }
}