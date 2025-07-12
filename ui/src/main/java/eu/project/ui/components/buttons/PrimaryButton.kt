package eu.project.ui.components.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.ui.components.text.labelLarge
import eu.project.ui.theme.Disabled
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

@Composable
fun primaryButton(
    text: String,
    onClick: () -> Unit,
    testTag: String
) {

    Button(

        modifier = Modifier
            .height(56.dp)
            .testTag(testTag),

        colors = ButtonColors(
            containerColor = Primary,
            contentColor = OnPrimary,
            disabledContainerColor = Disabled,
            disabledContentColor = SecondaryWhite
        ),

        onClick = { onClick() },

        content = {

            labelLarge(
                text = text,
                color = OnPrimary
            )
        }
    )
}