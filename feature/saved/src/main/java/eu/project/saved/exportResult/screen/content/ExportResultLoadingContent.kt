package eu.project.saved.exportResult.screen.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ColumnScope.exportResultLoadingContent() {

    // upper content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painter = painterResource(id = R.drawable.loading),
                contentDescription = stringResource(R.string.illustration_loading_description)
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

            headlineLarge(text = stringResource(R.string.loading_data))

            spacerV8()

            bodyLarge(
                text = stringResource(R.string.loading_data_explanation),
                color = SecondaryWhite,
                textAlign = TextAlign.Center
            )
        }
    )
}