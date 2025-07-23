package eu.project.common.model

import java.util.UUID

data class SavedWord(
    val uuid: UUID,
    val word: String,
    val language: String
)