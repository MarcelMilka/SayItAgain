package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.project.ui.R
import eu.project.ui.components.text.labelLarge
import eu.project.ui.theme.PrimaryWhite

@Composable
internal fun ColumnScope.savedWordsLoadingContent() {

    labelLarge(
        text = stringResource(R.string.loading),
        color = PrimaryWhite
    )
}