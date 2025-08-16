package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.theme.Background
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedWordsNoDataContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    var onNavigateSelectAudioScreenWasClicked = false

    @Before
    fun setUp() {

        onNavigateSelectAudioScreenWasClicked = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                savedWordsNoDataContent(
                    onNavigateSelectAudioScreen = { onNavigateSelectAudioScreenWasClicked = true }
                )
            }
        }
    }



    @Test
    fun illustrationEmpty_isDisplayed() {

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_empty_description))
            .assertIsDisplayed()
    }

    @Test
    fun headlineKindaEmpty_isDisplayed() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.kinda_empty))
            .assertIsDisplayed()
    }

    @Test
    fun bodyKindaEmptyExplanation_isDisplayed() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.kinda_empty_explanation))
            .assertIsDisplayed()
    }



//- primary button tests -----------------------------------------------------------------------------------------------

    @Test
    fun primaryButton_isDisplayed_and_isEnabled() {

        composeTestRule
            .onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE)
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun primaryButtonText_isDisplayed() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.pick_and_transcribe))
            .assertIsDisplayed()
    }

    @Test
    fun primaryButton_triggersLambdaWhenClicked() {

        composeTestRule
            .onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE)
            .performClick()

        assertTrue(onNavigateSelectAudioScreenWasClicked)
    }
}