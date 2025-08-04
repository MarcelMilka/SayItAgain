package eu.project.floatingActionButton.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.common.navigation.Screens
import eu.project.floatingActionButton.model.FloatingActionButtonViewState
import eu.project.floatingActionButton.model.FloatingActionButtonVisibilityState
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FloatingActionButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var visibilityState: MutableStateFlow<FloatingActionButtonVisibilityState>
    private lateinit var viewState: MutableStateFlow<FloatingActionButtonViewState>
    private lateinit var currentScreen: MutableStateFlow<String?>
    private var onNavigateExportWordsWasClicked = false

    @Before
    fun setUp() {

        visibilityState = MutableStateFlow(FloatingActionButtonVisibilityState.Hidden)
        viewState = MutableStateFlow(FloatingActionButtonViewState())
        currentScreen = MutableStateFlow(Screens.HOME)
        onNavigateExportWordsWasClicked = false

        composeTestRule.setContent {

            val visibility by visibilityState.collectAsState()
            val state by viewState.collectAsState()
            val screen by currentScreen.collectAsState()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {

                    floatingActionButton(
                        visibilityState = visibility,
                        viewState = state,
                        currentScreen = screen,
                        onNavigateExportWords = { onNavigateExportWordsWasClicked = true }
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                containerColor = Background,
                content = {

                    Box(
                        modifier = Modifier.fillMaxSize().background(Background),
                        content = {}
                    )
                }
            )
        }
    }



    // visibilityState
    @Test
    fun visibilityStateHidden_contentIsHidden() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Hidden

        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithTag(TestTags.FLOATING_ACTION_BUTTON)
            .assertIsNotDisplayed()
    }

    @Test
    fun visibilityStateVisible_contentIsVisible() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Visible

        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithTag(TestTags.FLOATING_ACTION_BUTTON)
            .assertIsDisplayed()
    }



    // viewState
    @Test
    fun viewStateDefault_displaysCorrectCorrectText() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Visible


        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithText(context.getString(R.string.export_words))
            .assertIsDisplayed()
    }



    // currentScreen
    @Test
    fun currentScreenSavedWords_clickingFab_callsOnNavigateExportWords() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Visible
        currentScreen.value = Screens.SAVED_WORDS

        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithTag(TestTags.FLOATING_ACTION_BUTTON)
            .performClick()

        assertTrue(onNavigateExportWordsWasClicked)
    }

    @Test
    fun currentScreenNotSavedWords_clickingFab_doesNotCallOnNavigateExportWords() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Visible
        currentScreen.value = Screens.HOME

        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithTag(TestTags.FLOATING_ACTION_BUTTON)
            .performClick()

        assertFalse(onNavigateExportWordsWasClicked)
    }

    @Test
    fun currentScreenNull_clickingFab_doesNotCallOnNavigateExportWords() = runTest {

        // set up
        visibilityState.value = FloatingActionButtonVisibilityState.Visible
        currentScreen.value = null

        // test
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithTag(TestTags.FLOATING_ACTION_BUTTON)
            .performClick()

        assertFalse(onNavigateExportWordsWasClicked)
    }
}