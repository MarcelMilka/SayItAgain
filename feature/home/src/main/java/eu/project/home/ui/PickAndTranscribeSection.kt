package eu.project.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.components.buttons.primaryButton
import eu.project.ui.components.spacers.spacerV16
import eu.project.ui.components.text.headlineLarge

@Composable
internal fun pickAndTranscribeSection(onClick: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            headlineLarge(text = stringResource(R.string.struggling_to_catch_every_word))

            spacerV16()

            primaryButton(
                text = stringResource(R.string.pick_and_transcribe),
                onClick = { onClick() },
                testTag = TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE
            )
        }
    )
}