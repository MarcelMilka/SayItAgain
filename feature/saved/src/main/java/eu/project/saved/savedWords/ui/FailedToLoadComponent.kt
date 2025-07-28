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
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineLarge
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun BoxScope.failedToLoadComponent(cause: String?) {

    Column(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = stringResource(R.string.illustration_error_description)
            )

            spacerV8()

            headlineLarge(text = stringResource(R.string.something_is_off))

            spacerV8()

            bodyLarge(
                text = "$cause",
                color = SecondaryWhite,
                textAlign = TextAlign.Center
            )
        }
    )
}