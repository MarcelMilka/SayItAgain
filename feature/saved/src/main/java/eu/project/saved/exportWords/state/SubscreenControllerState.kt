package eu.project.saved.exportWords.state

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.Color.Companion.Transparent
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

internal data class SubscreenControllerState(
    val selectWordsButtonState: SubscreenControllerButtonState = SubscreenControllerButtonVariants.selectWordsDisabled,
    val exportSettingsButtonState: SubscreenControllerButtonState = SubscreenControllerButtonVariants.exportSettingsEnabled
)

internal enum class ExportWordsSubscreen {
    SelectWords,
    ExportSettings
}

internal data class SubscreenControllerButtonState(
    val text: Int,
    val enabled: Boolean,
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

internal object SubscreenControllerButtonVariants {

    val selectWordsDisabled = SubscreenControllerButtonState(
        text = R.string.select_words,
        enabled = false,
        containerColor = Primary,
        contentColor = OnPrimary,
        borderColor = Primary
    )

    val selectWordsEnabled = SubscreenControllerButtonState(
        text = R.string.select_words,
        enabled = true,
        containerColor = Transparent,
        contentColor = SecondaryWhite,
        borderColor = SecondaryWhite
    )

    val exportSettingsDisabled = SubscreenControllerButtonState(
        text = R.string.export_settings,
        enabled = false,
        containerColor = Primary,
        contentColor = OnPrimary,
        borderColor = Primary
    )

    val exportSettingsEnabled = SubscreenControllerButtonState(
        text = R.string.export_settings,
        enabled = true,
        containerColor = Transparent,
        contentColor = SecondaryWhite,
        borderColor = SecondaryWhite
    )
}