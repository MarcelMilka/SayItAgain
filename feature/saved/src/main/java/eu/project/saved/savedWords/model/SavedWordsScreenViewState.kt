package eu.project.saved.savedWords.model

import eu.project.common.model.SavedWord

internal sealed class SavedWordsScreenViewState {

    data object Loading: SavedWordsScreenViewState()

    sealed class Loaded: SavedWordsScreenViewState() {

        data object NoData: Loaded()

        data class Data(val retrievedData: List<SavedWord>): Loaded()
    }

    data class FailedToLoad(val cause: String?): SavedWordsScreenViewState()
}