package eu.project.saved.exportWords.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.saved.exportWords.model.ExportMethodState
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.bodySmall
import eu.project.ui.components.text.headlineSmall
import eu.project.ui.components.text.labelLarge
import eu.project.ui.dimensions.WidgetCornerRadius
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun exportMethodSelector(
    uiState: ExportWordsUiState,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        content = {

            headlineSmall(stringResource(R.string.how_would_you_like_to_export_your_words))

            spacerV8()

            exportMethod(
                uiState.exportMethodPickerState.sendMethodState,
                onClick = { onClickSendMethod() }
            )

            spacerV8()

            exportMethod(
                uiState.exportMethodPickerState.downloadMethodState,
                onClick = { onClickDownloadMethod() }
            )
        }
    )
}

@Composable
internal fun ColumnScope.exportMethod(
    state: ExportMethodState,
    onClick: () -> Unit
) {

    Surface(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        color = state.backgroundColor,
        enabled = state.enabled,
        border = BorderStroke(2.dp, state.borderColor),
        shape = RoundedCornerShape(WidgetCornerRadius),
        content = {

            Column(
                modifier = Modifier
                    .padding(WidgetPadding.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    labelLarge(
                        text = stringResource(state.label),
                        color = state.labelColor
                    )

                    spacerV8()

                    bodySmall(
                        text = stringResource(state.body),
                        color = state.bodyColor,
                        textAlign = TextAlign.Start
                    )
                }
            )
        }
    )
}