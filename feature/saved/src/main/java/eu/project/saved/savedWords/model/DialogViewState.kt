package eu.project.saved.savedWords.model

import eu.project.common.model.SavedWord

internal sealed class DialogViewState {

    data object Hidden: DialogViewState()
    data class Visible(val wordToDelete: SavedWord): DialogViewState()
}