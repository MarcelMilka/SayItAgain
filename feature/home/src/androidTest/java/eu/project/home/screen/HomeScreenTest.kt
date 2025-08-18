package eu.project.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.theme.Background
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var isNetworkAvailable: MutableStateFlow<Boolean>
    private var isSelectAudioScreenNavigated = false
    private var isSavedWordsScreenNavigated = false

    @Before
    fun setUp() {

        isNetworkAvailable = MutableStateFlow(true)
        isSelectAudioScreenNavigated = false
        isSavedWordsScreenNavigated = false

        composeTestRule.setContent {

            Box(Modifier.background(Background)) {

                homeScreen(
                    isNetworkAvailable = isNetworkAvailable.collectAsState().value,
                    onNavigateSelectAudioScreen = { isSelectAudioScreenNavigated = true },
                    onNavigateSavedWordsScreen = { isSavedWordsScreenNavigated = true }
                )
            }
        }
    }



//- screen visibility tests ---------------------------------------------------------------------------------------------

    @Test
    fun homeScreen_isDisplayed_whenNetworkIsAvailable() {

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN).assertIsDisplayed()
    }

    @Test
    fun homeScreen_isDisplayed_whenNetworkIsUnavailable() {

        // setup
        isNetworkAvailable.update { false }

        // test
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN).assertIsDisplayed()
    }



//- content visibility tests --------------------------------------------------------------------------------------------

    @Test
    fun homeScreen_displaysMainContent_whenNetworkIsAvailable() {

        // headline
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()

        // primary button
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()

        // text button
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.my_vocabulary)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysMainContent_whenNetworkIsUnavailable() {

        // setup
        isNetworkAvailable.update { false }

        // test
        // headline
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()

        // primary button
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()

        // text button
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.my_vocabulary)).assertIsDisplayed()
    }



//- network banner visibility tests -------------------------------------------------------------------------------------

    @Test
    fun homeScreen_noConnectionBanner_isVisible_whenNetworkIsUnavailable() {

        // setup
        isNetworkAvailable.update { false }

        // test
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline_explanation)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_noConnectionBanner_isNotVisible_whenNetworkIsAvailable() {

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline_explanation)).assertDoesNotExist()
    }



//- network state transition tests --------------------------------------------------------------------------------------

    @Test
    fun homeScreen_transitionsFromNetworkAvailableToUnavailable_correctly() {

        // test initial state
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertDoesNotExist()

        // transition to network unavailable
        isNetworkAvailable.update { false }

        // verify banner is now visible
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline_explanation)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_transitionsFromNetworkUnavailableToAvailable_correctly() {

        // setup
        isNetworkAvailable.update { false }

        // test initial state
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertIsDisplayed()

        // transition to network available
        isNetworkAvailable.update { true }

        // verify banner is now hidden
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline_explanation)).assertDoesNotExist()
    }

    @Test
    fun homeScreen_mainContentRemainsVisible_duringNetworkTransitions() {

        // verify main content is visible
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()

        // transition to network unavailable
        isNetworkAvailable.update { false }

        // verify main content remains visible
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()

        // transition back to network available
        isNetworkAvailable.update { true }

        // verify main content still visible
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()
    }



//- user interaction tests ----------------------------------------------------------------------------------------------

    @Test
    fun homeScreen_pickAndTranscribeButtonClick_triggersCallback() {

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).performClick()

        assertTrue(isSelectAudioScreenNavigated)
    }

    @Test
    fun homeScreen_myVocabularyButtonClick_triggersCallback() {

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).performClick()

        assertTrue(isSavedWordsScreenNavigated)
    }

    @Test
    fun homeScreen_buttonsAreClickable_whenNetworkIsUnavailable() {

        // setup
        isNetworkAvailable.update { false }

        // test
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsEnabled()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsEnabled()
    }

    @Test
    fun homeScreen_buttonsAreClickable_whenNetworkIsAvailable() {

        // test
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsEnabled()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsEnabled()
    }

    @Test
    fun homeScreen_multipleButtonClicks_workCorrectly() {

        // test primary button multiple clicks
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).performClick()
        assertTrue(isSelectAudioScreenNavigated)

        // test text button multiple clicks
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).performClick()
        assertTrue(isSavedWordsScreenNavigated)

        // reset callback
        isSavedWordsScreenNavigated = false

        // test primary button again
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).performClick()
        assertTrue(isSelectAudioScreenNavigated)
    }



//- edge case tests ----------------------------------------------------------------------------------------------------

    @Test
    fun homeScreen_rapidNetworkStateChanges_handleCorrectly() {

        // test rapid transitions
        isNetworkAvailable.update { false }
        isNetworkAvailable.update { true }
        isNetworkAvailable.update { false }
        isNetworkAvailable.update { true }

        // verify final state is correct
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_NO_CONNECTION_BANNER).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.you_are_offline)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.struggling_to_catch_every_word)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).assertIsDisplayed()
    }

    @Test
    fun homeScreen_buttonsWorkCorrectly_whenNetworkStateChanges() {

        // test button in network available state
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE).performClick()
        assertTrue(isSelectAudioScreenNavigated)

        // reset callback
        isSelectAudioScreenNavigated = false

        // change to network unavailable
        isNetworkAvailable.update { false }

        // test button still works in network unavailable state
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY).performClick()
        assertTrue(isSavedWordsScreenNavigated)
    }
}