package eu.project.saved.exportWords.screen.content

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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportWordsErrorContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportWordsErrorContent()
            }
        }
    }



    @Test
    fun illustrationError_isDisplayed() = runTest {

        composeTestRule
            .onNodeWithContentDescription(
                context.getString(R.string.illustration_error_description)
            )
            .assertIsDisplayed()
    }

    @Test
    fun headlineSomethingWentWrong_isDisplayed() = runTest {

        composeTestRule
            .onNodeWithText(
                context.getString(R.string.something_went_wrong)
            )
            .assertIsDisplayed()
    }

    @Test
    fun bodySomethingWentWrongExplanation_isDisplayed() = runTest {

        composeTestRule
            .onNodeWithText(
                context.getString(R.string.something_went_wrong_explanation)
            )
            .assertIsDisplayed()
    }
}