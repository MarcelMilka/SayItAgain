package eu.project.common.navigation

import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    data object HomeScreen: Navigation()

    sealed class Saved: Navigation() {

        @Serializable
        data object RouteSaved: Saved()

            @Serializable
            data object SavedWordsScreen: Saved()

            @Serializable
            data object ExportWordsScreen: Saved()
    }

    sealed class Transcribe: Navigation() {

        @Serializable
        data object RouteTranscribe: Transcribe()

            @Serializable
            data object SelectAudioScreen: Transcribe()
    }
}