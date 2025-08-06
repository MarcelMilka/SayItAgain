package eu.project.saved.exportWords.ui

import androidx.compose.ui.graphics.Color
import eu.project.saved.exportWords.model.ExportWordsSubscreen

import androidx.compose.ui.graphics.Color.Companion.Transparent
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

internal data class SubscreenControllerState(
    val isVisible: Boolean = true,
    val exportWordsSubscreen: ExportWordsSubscreen = ExportWordsSubscreen.SelectWords,
    val leftButton: SubscreenControllerButtonState = SubscreenControllerButtonVariants.leftActive,
    val rightButton: SubscreenControllerButtonState = SubscreenControllerButtonVariants.rightInactive
)

internal data class SubscreenControllerButtonState(
    val text: Int,
    val enabled: Boolean,
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

internal object SubscreenControllerButtonVariants {

    val leftActive = SubscreenControllerButtonState(
        text = R.string.select_words,
        enabled = false,
        containerColor = Primary,
        contentColor = OnPrimary,
        borderColor = Primary
    )

    val leftInactive = SubscreenControllerButtonState(
        text = R.string.select_words,
        enabled = true,
        containerColor = Transparent,
        contentColor = SecondaryWhite,
        borderColor = SecondaryWhite
    )

    val rightActive = SubscreenControllerButtonState(
        text = R.string.export_settings,
        enabled = false,
        containerColor = Primary,
        contentColor = OnPrimary,
        borderColor = Primary
    )

    val rightInactive = SubscreenControllerButtonState(
        text = R.string.select_words,
        enabled = true,
        containerColor = Transparent,
        contentColor = SecondaryWhite,
        borderColor = SecondaryWhite
    )
}