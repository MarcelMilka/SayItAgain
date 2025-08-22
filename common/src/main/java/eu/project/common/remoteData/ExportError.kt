package eu.project.common.remoteData

sealed class ExportError : Exception() {
    data class ValidationError(val messages: List<String>) : ExportError()
    data class UnknownError(override val message: String) : ExportError()
}