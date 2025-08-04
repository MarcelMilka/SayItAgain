package eu.project.floatingActionButton.model

internal sealed class FloatingActionButtonVisibilityState {

    data object Hidden: FloatingActionButtonVisibilityState()
    data object Visible: FloatingActionButtonVisibilityState()
}