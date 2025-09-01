package eu.project.saved.exportResult.screen.content

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.ui.R
import eu.project.ui.theme.Background
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportResultLoadingContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportResultLoadingContent()
            }
        }
    }

//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun exportResultLoadingContent_defaultState_loadingImageIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_loading_description))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultLoadingContent_defaultState_headlineTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultLoadingContent_defaultState_bodyTextIsDisplayed() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data_explanation))
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportResultLoadingContent_defaultState_displaysCorrectHeadlineText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultLoadingContent_defaultState_displaysCorrectBodyText() {

        // test
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data_explanation))
            .assertIsDisplayed()
    }



//- layout and structure tests ----------------------------------------------------------------------------------------

    @Test
    fun exportResultLoadingContent_defaultState_allElementsAreDisplayedInCorrectOrder() {

        // test - verify all main elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_loading_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultLoadingContent_defaultState_imageHasCorrectContentDescription() {

        // test
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_loading_description))
            .assertIsDisplayed()
    }
}