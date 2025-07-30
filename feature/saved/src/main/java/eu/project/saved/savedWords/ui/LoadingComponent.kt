package eu.project.saved.savedWords.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.project.ui.R
import eu.project.ui.components.text.labelLarge
import eu.project.ui.theme.PrimaryWhite

@Composable
internal fun BoxScope.loadingComponent() {

    Column(
        modifier = Modifier
            .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            labelLarge(
                text = stringResource(R.string.loading),
                color = PrimaryWhite
            )
        }
    )
}