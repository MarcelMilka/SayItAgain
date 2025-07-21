package eu.project.ui.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.ui.dimensions.ScreenPadding
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.SayItAgainTypography

@Composable
fun headlineLarge(text: String) {

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ScreenPadding.dp, vertical = 0.dp),
        color = PrimaryWhite,
        textAlign = TextAlign.Center,
        style = SayItAgainTypography.headlineLarge
    )
}

@Composable
fun headlineMedium(text: String) {

    Text(
        text = text,
        color = PrimaryWhite,
        textAlign = TextAlign.Start,
        style = SayItAgainTypography.headlineMedium
    )
}

@Composable
fun headlineSmall(text: String) {

    Text(
        text = text,
        color = PrimaryWhite,
        textAlign = TextAlign.Start,
        style = SayItAgainTypography.headlineSmall
    )
}