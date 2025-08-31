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

class ExportResultReadyToSaveFileContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onClickSaveExportedWordsWasCalled = false
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        onClickSaveExportedWordsWasCalled = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportResultReadyToSaveFileContent(
                    onClickSaveExportedWords = { onClickSaveExportedWordsWasCalled = true }
                )
            }
        }
    }

//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_downloadImageIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_download_description))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_headlineTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_bodyTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_saveButtonIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_displaysCorrectHeadlineText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_displaysCorrectBodyText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_displaysCorrectButtonText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.save_exported_words))
            .assertIsDisplayed()
    }



//- button interaction tests -------------------------------------------------------------------------------------------

    @Test
    fun exportResultReadyToSaveFileContent_saveButtonClicked_callsOnClickSaveExportedWords() {

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsEnabled()
            .performClick()

        // verify
        assertEquals(true, onClickSaveExportedWordsWasCalled)
    }



//- layout and structure tests ----------------------------------------------------------------------------------------

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_allElementsAreDisplayedInCorrectOrder() {

        // test - verify all main elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_download_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save_explanation))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultReadyToSaveFileContent_defaultState_imageHasCorrectContentDescription() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_download_description))
            .assertIsDisplayed()
    }
}
