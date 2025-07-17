package eu.project.common.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Navigation {

    @Serializable
    data object HomeScreen: Navigation()
}