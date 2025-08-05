package eu.project.saved.exportWords.model

internal sealed class ExportWordsScreenState {
    data object Initial: ExportWordsScreenState()
    data object Disconnected: ExportWordsScreenState()
    data object Error: ExportWordsScreenState()

    data object ReadyToExport: ExportWordsScreenState()
}