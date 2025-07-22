package eu.project.topBar.vm

import app.cash.turbine.test
import eu.project.common.navigation.Screens
import eu.project.topBar.model.TopBarViewState
import eu.project.ui.R
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TopBarViewModelTest {

    private lateinit var viewModel: TopBarViewModel

    @Before
    fun setUp() {
        viewModel = TopBarViewModel()
    }

    @Test
    fun `onRouteChanged updates uiState for each route`() = runTest {
        viewModel.uiState.test {

            // Home is default state
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = false,
                    screenName = R.string.home,
                    showInfoIcon = true
                ),
                awaitItem()
            )

            // null -> error fallback
            viewModel.onRouteChanged(null)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = false,
                    screenName = R.string.something_went_wrong_while_navigating,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Saved Words
            viewModel.onRouteChanged(Screens.SAVED_WORDS)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = true,
                    screenName = R.string.saved_words,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Export Words
            viewModel.onRouteChanged(Screens.EXPORT_WORDS)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = true,
                    screenName = R.string.export_words,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Select Audio
            viewModel.onRouteChanged(Screens.SELECT_AUDIO_SCREEN)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = true,
                    screenName = R.string.select_audio,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Select Language
            viewModel.onRouteChanged(Screens.SELECT_LANGUAGE_SCREEN)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = true,
                    screenName = R.string.select_language,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Transcript
            viewModel.onRouteChanged(Screens.TRANSCRIPT)
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = true,
                    screenName = R.string.transcript,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Generating Transcript
            viewModel.onRouteChanged(Screens.GENERATING_TRANSCRIPT_SCREEN)
            assertEquals(
                TopBarViewState(
                    displayTopBar = false,
                    showBackIcon = false,
                    screenName = R.string.generating_transcript,
                    showInfoIcon = false
                ),
                awaitItem()
            )

            // Unknown screen
            viewModel.onRouteChanged("UnknownScreen")
            assertEquals(
                TopBarViewState(
                    displayTopBar = true,
                    showBackIcon = false,
                    screenName = R.string.something_went_wrong_while_navigating,
                    showInfoIcon = false
                ),
                awaitItem()
            )
        }
    }
}
