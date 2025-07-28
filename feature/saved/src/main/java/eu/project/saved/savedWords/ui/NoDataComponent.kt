package eu.project.saved.savedWords.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.components.buttons.primaryButton
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineLarge
import eu.project.ui.dimensions.ThumbReachPadding
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun BoxScope.noDataComponent(onNavigateSelectAudioScreen: () -> Unit) {

    Column(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painter = painterResource(id = R.drawable.empty),
                contentDescription = stringResource(R.string.illustration_error_description)
            )

            spacerV8()

            headlineLarge(text = stringResource(R.string.kinda_empty))

            spacerV8()

            bodyLarge(
                text = stringResource(R.string.once_you_save_your_first_word),
                color = SecondaryWhite,
                textAlign = TextAlign.Center
            )
        }
    )

    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = ThumbReachPadding.dp),
        content = {

            primaryButton(
                text = stringResource(R.string.pick_and_transcribe),
                onClick = { onNavigateSelectAudioScreen() },
                testTag = TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE
            )
        }
    )
}