package eu.project.floatingActionButton.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.floatingActionButton.model.FloatingActionButtonViewState
import eu.project.ui.components.text.labelLarge
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun floatingActionButtonContent(viewState: FloatingActionButtonViewState) {

    Box(
        modifier = Modifier.padding(horizontal = WidgetPadding.dp),
        contentAlignment = Alignment.Center,
        content = {

            labelLarge(
                text = stringResource(viewState.content),
                color = viewState.contentColor
            )
        }
    )
}