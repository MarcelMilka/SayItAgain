package eu.project.common.localData

import eu.project.common.model.SavedWord

sealed class SavedWordsRepositoryDataState {

    data object Loading: SavedWordsRepositoryDataState()

    sealed class Loaded: SavedWordsRepositoryDataState() {

        data object NoData: Loaded()

        data class Data(val retrievedData: List<SavedWord>): Loaded()
    }

    data class FailedToLoad(val cause: String?): SavedWordsRepositoryDataState()
}