package eu.project.home.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import eu.project.common.TestTags
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var isSelectAudioScreenNavigated = false
    private var isSavedWordsScreenNavigated = false

    @Before
    fun setUp() {
        // Reset navigation flags before each test
        isSelectAudioScreenNavigated = false
        isSavedWordsScreenNavigated = false
    }

    private fun setContent(isNetworkAvailable: Boolean) {
        composeTestRule.setContent {
            homeScreen(
                isNetworkAvailable = isNetworkAvailable,
                onNavigateSelectAudioScreen = { isSelectAudioScreenNavigated = true },
                onNavigateSavedWordsScreen = { isSavedWordsScreenNavigated = true }
            )
        }
    }

    @Test
    fun noConnectionBanner_isVisible_whenNetworkIsUnavailable() {
        setContent(isNetworkAvailable = false)

        composeTestRule
            .onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER)
            .assertIsDisplayed()
    }

    @Test
    fun noConnectionBanner_isNotVisible_whenNetworkIsAvailable() {
        setContent(isNetworkAvailable = true)

        composeTestRule
            .onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER)
            .assertIsNotDisplayed()
    }

    @Test
    fun pickAndTranscribeSection_buttonClick_triggersCallback() {
        setContent(isNetworkAvailable = true)

        composeTestRule
            .onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE)
            .performClick()

        assertTrue(isSelectAudioScreenNavigated)
    }

    @Test
    fun textButton_myVocabularyButtonClick_triggersCallback() {
        setContent(isNetworkAvailable = true)

        composeTestRule
            .onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY)
            .performClick()

        assertTrue(isSavedWordsScreenNavigated)
    }
}