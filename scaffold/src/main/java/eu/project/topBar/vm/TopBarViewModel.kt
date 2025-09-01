package eu.project.topBar.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.navigation.Screens
import eu.project.topBar.model.TopBarViewState
import eu.project.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class TopBarViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(TopBarViewState())
    val uiState = _uiState.asStateFlow()

    fun onRouteChanged(route: String?) {

        val state = when(route) {
            Screens.HOME -> TopBarViewState(true, false, R.string.home, true)

            Screens.SAVED_WORDS -> TopBarViewState(true, true, R.string.saved_words, false)
            Screens.EXPORT_WORDS -> TopBarViewState(true, true, R.string.export_words, false)
            Screens.EXPORT_RESULT -> TopBarViewState(true, true, R.string.export_result, false)
            Screens.SELECT_AUDIO_SCREEN -> TopBarViewState(true, true, R.string.select_audio, false)
            Screens.SELECT_LANGUAGE_SCREEN -> TopBarViewState(true, true, R.string.select_language, false)
            Screens.TRANSCRIPT -> TopBarViewState(true, true, R.string.transcript, false)

            Screens.GENERATING_TRANSCRIPT_SCREEN -> TopBarViewState(false, false, R.string.generating_transcript, false)

            else -> TopBarViewState(true, false, R.string.something_went_wrong_while_navigating, false)
        }

        _uiState.value = state
    }
}