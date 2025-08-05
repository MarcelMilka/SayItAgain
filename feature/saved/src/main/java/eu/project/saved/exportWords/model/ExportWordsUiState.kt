package eu.project.saved.exportWords.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

internal data class ExportWordsUiState(
    val exportableWords: List<ExportableSavedWord> = listOf(),
    val subscreenControllerUiState: SubscreenControllerUiState = SubscreenControllerUiState()
)


internal data class SubscreenControllerUiState(
    val subscreen: ExportWordsSubscreen = ExportWordsSubscreen.ExportSettings,
    val leftButtonUiState: SubscreenControllerButtonUiState = SubscreenControllerButtonUiState(
        text = R.string.select_words,
        enabled = false,
        containerColor = Primary,
        contentColor = OnPrimary,
        borderColor = Primary
    ),
    val rightButtonUiState: SubscreenControllerButtonUiState = SubscreenControllerButtonUiState(
        text = R.string.export_settings,
        enabled = true,
        containerColor = Transparent,
        contentColor = SecondaryWhite,
        borderColor = SecondaryWhite
    ),
)

internal data class SubscreenControllerButtonUiState(
    val text: Int,
    val enabled: Boolean,
    val containerColor: Color,
    val borderColor: Color,
    val contentColor: Color,
)