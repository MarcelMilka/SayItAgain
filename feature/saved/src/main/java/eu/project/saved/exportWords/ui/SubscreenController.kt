package eu.project.saved.exportWords.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.graphics.Color.Companion.Transparent
import eu.project.ui.theme.SecondaryWhite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.ui.R
import eu.project.ui.components.text.labelLarge
import eu.project.ui.dimensions.BetweenWidgetsPadding

@Composable
internal fun subscreenController(
    subscreenControllerState: SubscreenControllerState,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit
) {

    Row(
        modifier = Modifier.padding(bottom = BetweenWidgetsPadding.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {

            subscreenControllerButton(
                buttonState = subscreenControllerState.leftButton,
                testTag = "selectWords",
                onClick = { onClickLeft() }
            )

            Spacer(modifier = Modifier.width(4.dp))

            subscreenControllerButton(
                buttonState = subscreenControllerState.rightButton,
                testTag = "exportSettings",
                onClick = { onClickRight() }
            )
        }
    )
}

@Composable
internal fun RowScope.subscreenControllerButton(
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