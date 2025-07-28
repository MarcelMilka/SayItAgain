package eu.project.ui.components.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.ui.components.text.labelLarge
import eu.project.ui.theme.Primary
import eu.project.ui.theme.PrimaryWhite

@Composable
fun secondaryButton(
    text: String,
    onClick: () -> Unit,
    testTag: String
) {

    Button(

        modifier = Modifier
            .height(56.dp)
            .testTag(testTag),

        colors = ButtonColors(
            containerColor = Transparent,
            contentColor = PrimaryWhite,
            disabledContainerColor = Transparent,
            disabledContentColor = PrimaryWhite
        ),

        onClick = { onClick() },

        content = {

            labelLarge(
                text = text,
                color = Primary
            )
        }
    )
}