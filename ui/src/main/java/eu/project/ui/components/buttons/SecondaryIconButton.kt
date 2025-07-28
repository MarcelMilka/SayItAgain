package eu.project.ui.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.ui.dimensions.MinTouchTarget
import eu.project.ui.theme.SecondaryWhite

@Composable
fun secondaryIconButton(
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String,
    testTag: String
) {

    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .size(MinTouchTarget.dp)
            .testTag(testTag),
        content = {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = SecondaryWhite
            )
        }
    )
}