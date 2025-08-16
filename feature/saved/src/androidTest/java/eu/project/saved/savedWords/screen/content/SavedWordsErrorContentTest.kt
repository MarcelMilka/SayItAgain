package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedWordsErrorContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var cause: MutableStateFlow<String?>
    private val defaultCause = "Exemplary cause"

    @Before
    fun setUp() {

        cause = MutableStateFlow(defaultCause)

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                savedWordsErrorContent(
                    cause = cause.collectAsState().value
                )
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

    @Test
    fun bodyErrorCause_isDisplayed_whenCauseIsNotNull() = runTest {

        composeTestRule
            .onNodeWithText(defaultCause)
            .assertIsDisplayed()
    }

    @Test
    fun bodyNull_isDisplayed_whenCauseIsNull() = runTest {

        // setup
        cause.update { null }

        // test
        composeTestRule
            .onNodeWithText("null")
            .assertIsDisplayed()
    }
}