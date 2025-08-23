package eu.project.saved.exportWords.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.saved.exportWords.state.ExportWordsButtonState
import eu.project.ui.components.text.labelLarge
import eu.project.ui.dimensions.ThumbReachPadding
import eu.project.ui.theme.OnPrimary

@Composable
internal fun exportWordsButton(
    state: ExportWordsButtonState,
    onClick: () -> Unit,
    testTag: String
) {

    val animatedContainerColor by animateColorAsState(targetValue = state.containerColor)
    val animatedContentColor by animateColorAsState(targetValue = state.contentColor)

    Button(
        modifier = Modifier
            .padding(bottom = ThumbReachPadding.dp)
            .height(56.dp)
            .testTag(testTag),
        colors = ButtonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor,
            disabledContainerColor = animatedContainerColor,
            disabledContentColor = animatedContentColor
        ),
        enabled = state.enabled,
        onClick = { onClick() },
        content = {

            labelLarge(
                text = stringResource(state.content),
                color = OnPrimary
            )
        }
    )
}