package eu.project.saved.savedWords.state

import eu.project.common.model.SavedWord

internal sealed class DiscardWordDialogState {
    data object Hidden: DiscardWordDialogState()
    data class Visible(val wordToDelete: SavedWord): DiscardWordDialogState()
}