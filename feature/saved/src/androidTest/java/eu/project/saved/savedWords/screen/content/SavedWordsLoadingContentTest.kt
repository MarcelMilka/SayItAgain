package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.ui.R
import eu.project.ui.theme.Background
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedWordsLoadingContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                savedWordsLoadingContent()
            }
        }
    }



    @Test
    fun labelLarge_isDisplayed() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.loading))
            .assertIsDisplayed()
    }
}