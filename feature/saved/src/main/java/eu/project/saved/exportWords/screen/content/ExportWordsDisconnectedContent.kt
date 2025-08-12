package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.spacers.spacerVertical32
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineLarge
import eu.project.ui.theme.SecondaryWhite

@Composable
fun BoxScope.exportWordsDisconnectedContent() {

    // upper content
    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .fillMaxHeight(0.50f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painter = painterResource(id = R.drawable.offline),
                contentDescription = stringResource(R.string.illustration_offline_description)
            )
        }
    )

    // bottom content
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .fillMaxHeight(0.50f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            spacerVertical32()

            headlineLarge(text = stringResource(R.string.no_internet_connection))

            spacerV8()

            bodyLarge(
                text = stringResource(R.string.no_internet_connection_explanation),
                color = SecondaryWhite,
                textAlign = TextAlign.Center
            )
        }
    )

}