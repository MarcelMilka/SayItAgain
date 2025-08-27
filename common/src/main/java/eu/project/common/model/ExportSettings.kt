package eu.project.common.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ExportSettings(val wordsToExport: List<Word>)

@Serializable
data class Word(val value: String)

fun ExportSettings.encodeToString(): String {

    return Json.encodeToString(this)
}

fun String.decodeToExportSettings(): ExportSettings {

    return Json.decodeFromString<ExportSettings>(this)
}