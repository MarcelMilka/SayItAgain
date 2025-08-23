package eu.project.saved.exportWords.state

import androidx.compose.ui.graphics.Color
import eu.project.ui.R
import eu.project.ui.theme.Disabled
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

internal data class ExportWordsButtonState(
    val content: Int = R.string.export_words_button,
    val enabled: Boolean,
    val containerColor: Color,
    val contentColor: Color
)

internal object ExportWordsButtonVariants {

    val enabled = ExportWordsButtonState(
        enabled = true,
        containerColor = Primary,
        contentColor = OnPrimary
    )

    val disabled = ExportWordsButtonState(
        enabled = false,
        containerColor = Disabled,
        contentColor = SecondaryWhite
    )
}