package eu.project.transcribe.selectAudio.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.transcribe.selectAudio.screen.selectAudioScreen

fun NavGraphBuilder.selectAudioImpl() {

    composable<Navigation.Transcribe.SelectAudioScreen> {

        selectAudioScreen()
    }
}