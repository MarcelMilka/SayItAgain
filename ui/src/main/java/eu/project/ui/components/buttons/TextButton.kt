package eu.project.ui.components.buttons

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import eu.project.ui.components.text.labelLarge
import eu.project.ui.theme.PrimaryWhite

@Composable
fun textButton(
    text: String,
    onClick: () -> Unit,
    testTag: String
) {

    TextButton(

        modifier = Modifier.testTag(testTag),

        onClick = { onClick() },

        content = {

            labelLarge(
                text = text,
                color = PrimaryWhite
            )
        },
    )
}