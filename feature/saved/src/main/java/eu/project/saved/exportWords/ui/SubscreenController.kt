package eu.project.saved.exportWords.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.state.SubscreenControllerButtonState
import eu.project.saved.exportWords.state.SubscreenControllerState
import eu.project.ui.components.spacers.spacerHorizontal4
import eu.project.ui.components.text.labelLarge
import eu.project.ui.dimensions.BetweenWidgetsPadding

@Composable
internal fun subscreenController(
    subscreenControllerState: SubscreenControllerState,
    onSwitchToSelectWords: () -> Unit,
    onTryToSwitchToExportSettings: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(bottom = BetweenWidgetsPadding.dp)
            .testTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {

            subscreenControllerButton(
                buttonState = subscreenControllerState.selectWordsButtonState,
                testTag = TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON,
                onClick = onSwitchToSelectWords
            )

            spacerHorizontal4()

            subscreenControllerButton(
                buttonState = subscreenControllerState.exportSettingsButtonState,
                testTag = TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON,
                onClick = onTryToSwitchToExportSettings
            )
        }
    )
}

@Composable
private fun RowScope.subscreenControllerButton(
    buttonState: SubscreenControllerButtonState,
    testTag: String,
    onClick: () -> Unit
) {

    val animatedContainerColor by animateColorAsState(targetValue = buttonState.containerColor)
    val animatedContentColor by animateColorAsState(targetValue = buttonState.contentColor)
    val animatedBorderColor by animateColorAsState(targetValue = buttonState.borderColor)

    Button(
        modifier = Modifier
            .height(40.dp)
            .weight(1f)
            .testTag(testTag),
        onClick = onClick,
        border = BorderStroke(2.dp, animatedBorderColor),
        enabled = buttonState.enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor,
            disabledContainerColor = animatedContainerColor,
            disabledContentColor = animatedContentColor
        ),
        content = {

            labelLarge(
                text = stringResource(buttonState.text),
                color = animatedContentColor
            )
        }
    )
}