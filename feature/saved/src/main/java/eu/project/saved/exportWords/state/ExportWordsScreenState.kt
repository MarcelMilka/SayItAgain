package eu.project.saved.exportWords.state

internal sealed class ExportWordsScreenState {
    data object Loading: ExportWordsScreenState()
    data object Disconnected: ExportWordsScreenState()
    data object Error: ExportWordsScreenState()
    data object Loaded: ExportWordsScreenState()
}