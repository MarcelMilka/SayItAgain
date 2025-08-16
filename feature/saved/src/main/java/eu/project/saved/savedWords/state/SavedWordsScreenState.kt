package eu.project.saved.savedWords.state

import eu.project.common.model.SavedWord

internal sealed class SavedWordsScreenState {
    data object Loading: SavedWordsScreenState()
    data class Error(val cause: String?): SavedWordsScreenState()

    sealed class Loaded: SavedWordsScreenState() {
        data object NoData: Loaded()
        data class Data(val retrievedData: List<SavedWord>): Loaded()
    }
}