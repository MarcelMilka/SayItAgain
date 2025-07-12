package eu.project.ui.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import eu.project.ui.theme.SayItAgainTypography

@Composable
fun bodyLarge(
    text: String,
    color: Color,
    textAlign: TextAlign
) {

    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        style = SayItAgainTypography.bodyLarge
    )
}

@Composable
fun bodySmall(
    text: String,
    color: Color,
    textAlign: TextAlign
) {

    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        style = SayItAgainTypography.bodySmall
    )
}