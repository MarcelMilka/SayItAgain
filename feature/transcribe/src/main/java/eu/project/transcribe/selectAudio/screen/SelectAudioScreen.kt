package eu.project.transcribe.selectAudio.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.spacers.spacerVertical32
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineLarge
import eu.project.ui.dimensions.ScreenPadding
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun selectAudioScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(TestTags.SELECT_AUDIO_SCREEN),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        content = {

            // upper content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    Image(
                        painter = painterResource(id = R.drawable.development),
                        contentDescription = stringResource(R.string.illustration_development_description)
                    )
                }
            )

            // bottom content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    spacerVertical32()

                    headlineLarge(text = stringResource(R.string.coming_soon))

                    spacerV8()

                    bodyLarge(
                        text = stringResource(R.string.coming_soon_explanation),
                        color = SecondaryWhite,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    )
}