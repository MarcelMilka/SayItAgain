package eu.project.floatingActionButton.model

import androidx.compose.ui.graphics.Color
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary

internal data class FloatingActionButtonViewState(
    val content: Int = R.string.export_words,
    val enabled: Boolean = true,
    val containerColor: Color = Primary,
    val contentColor: Color = OnPrimary
)