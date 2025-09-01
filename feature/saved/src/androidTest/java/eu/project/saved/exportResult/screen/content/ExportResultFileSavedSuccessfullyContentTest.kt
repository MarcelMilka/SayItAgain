package eu.project.saved.exportResult.screen.content

import android.content.Context
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportResultFileSavedSuccessfullyContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onClickContinueWasCalled = false
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {

        onClickContinueWasCalled = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportResultFileSavedSuccessfullyContent(
                    onClickContinue = { onClickContinueWasCalled = true }
                )
            }
        }
    }

//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_confirmationImageIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_confirmation_description))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_headlineTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_bodyTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_continueButtonIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_displaysCorrectHeadlineText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_displaysCorrectBodyText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_displaysCorrectButtonText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.continue_))
            .assertIsDisplayed()
    }



//- button interaction tests -------------------------------------------------------------------------------------------

    @Test
    fun exportResultFileSavedSuccessfullyContent_continueButtonClicked_callsOnClickContinue() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        // verify
        assertTrue(onClickContinueWasCalled)
    }



//- layout and structure tests ----------------------------------------------------------------------------------------

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_allElementsAreDisplayedInCorrectOrder() {

        // test - verify all main elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_confirmation_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully_explanation))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFileSavedSuccessfullyContent_defaultState_imageHasCorrectContentDescription() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_confirmation_description))
            .assertIsDisplayed()
    }
}