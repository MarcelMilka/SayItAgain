package eu.project.transcribe.selectAudio.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.project.common.navigation.Navigation
import eu.project.ui.theme.Background
import eu.project.ui.theme.PrimaryWhite

fun NavGraphBuilder.selectAudioScreenImpl() {

    composable<Navigation.Transcribe.SelectAudioScreen> {

        Box(
            modifier = Modifier.fillMaxSize().background(Background),
            contentAlignment = Alignment.Center,
            content = { Text(text = "SelectAudioScreen", color = PrimaryWhite) }
        )
    }
}