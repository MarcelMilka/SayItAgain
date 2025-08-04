package eu.project.floatingActionButton.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.navigation.Screens
import eu.project.floatingActionButton.model.FloatingActionButtonViewState
import eu.project.floatingActionButton.model.FloatingActionButtonVisibilityState
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FloatingActionButtonViewModel @Inject constructor(savedWordsRepository: SavedWordsRepository): ViewModel() {

    private var _visibilityState = MutableStateFlow<FloatingActionButtonVisibilityState>(FloatingActionButtonVisibilityState.Hidden)
    val visibilityState = _visibilityState.asStateFlow()

    private var _viewState = MutableStateFlow<FloatingActionButtonViewState>(FloatingActionButtonViewState())
    val viewState = _viewState.asStateFlow()

    private var _dataState: StateFlow<SavedWordsRepositoryDataState> = savedWordsRepository.dataState

    private var _currentScreen = MutableStateFlow<String?>(Screens.HOME)
    val currentScreen = _currentScreen.asStateFlow()

    init {

        observeChanges()
    }

    fun onRouteChanged(route: String?) {

        _currentScreen.value = route
    }

    private fun observeChanges() {

        viewModelScope
            .launch {

                _currentScreen
                    .combine(_dataState) { currentScreen, dataState ->

                        CombinedFlows(
                            currentScreen = currentScreen,
                            dataState = dataState
                        )
                    }
                    .map { combinedFlows ->

                        CombinedStates(
                            visibilityState = determineVisibilityState(
                                currentScreen = combinedFlows.currentScreen,
                                dataState = combinedFlows.dataState
                            ),
                            viewState = determineViewState()
                        )
                    }
                    .distinctUntilChanged()
                    .collect { combinedStates ->

                        _visibilityState.value = combinedStates.visibilityState
                        _viewState.value = combinedStates.viewState
                    }
            }
    }

    private fun determineVisibilityState(
        currentScreen: String?,
        dataState: SavedWordsRepositoryDataState
    ): FloatingActionButtonVisibilityState {

        return when(currentScreen) {

            Screens.SAVED_WORDS -> {


                if (dataState is SavedWordsRepositoryDataState.Loaded.Data) {

                    FloatingActionButtonVisibilityState.Visible
                }

                else {

                    FloatingActionButtonVisibilityState.Hidden
                }
            }

            else -> {

                FloatingActionButtonVisibilityState.Hidden
            }
        }
    }

    // (no logic yet)
    private fun determineViewState(): FloatingActionButtonViewState {

        return FloatingActionButtonViewState(
            content = R.string.export_words,
            enabled = true,
            containerColor = Primary,
            contentColor = OnPrimary
        )
    }

    private data class CombinedFlows(
        val currentScreen: String?,
        val dataState: SavedWordsRepositoryDataState
    )

    private data class CombinedStates(
        val visibilityState: FloatingActionButtonVisibilityState,
        val viewState: FloatingActionButtonViewState
    )
}