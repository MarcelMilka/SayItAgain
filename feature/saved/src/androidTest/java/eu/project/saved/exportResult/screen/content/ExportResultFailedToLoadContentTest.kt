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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportResultFailedToLoadContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onClickTryAgainLaterWasCalled = false
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testThrowable = RuntimeException("Test error message")

    @Before
    fun setUp() {
        onClickTryAgainLaterWasCalled = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportResultFailedToLoadFileContent(
                    throwable = testThrowable,
                    onClickTryAgainLater = { onClickTryAgainLaterWasCalled = true }
                )
            }
        }
    }

//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_errorImageIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_error_description))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_headlineTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_errorMessageIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(testThrowable.message!!)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_tryAgainButtonIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_displaysCorrectHeadlineText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_displaysCorrectErrorMessage() {

        // test
        composeTestRule
            .onNodeWithText(testThrowable.message!!)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_displaysCorrectButtonText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.try_again_later))
            .assertIsDisplayed()
    }



//- button interaction tests -------------------------------------------------------------------------------------------

    @Test
    fun exportResultFailedToLoadFileContent_tryAgainButtonClicked_callsOnClickTryAgainLater() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsEnabled()
            .performClick()

        // verify
        assertEquals(true, onClickTryAgainLaterWasCalled)
    }



//- layout and structure tests ----------------------------------------------------------------------------------------

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_allElementsAreDisplayedInCorrectOrder() {

        // test - verify all main elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_error_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(testThrowable.message!!)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultFailedToLoadFileContent_defaultState_imageHasCorrectContentDescription() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_error_description))
            .assertIsDisplayed()
    }
}