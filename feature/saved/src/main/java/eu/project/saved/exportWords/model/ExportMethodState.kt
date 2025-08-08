package eu.project.saved.exportWords.model

import androidx.compose.ui.graphics.Color

data class ExportMethodState(
    val label: Int,
    val body: Int,
    val enabled: Boolean,
    val labelColor: Color,
    val bodyColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
)