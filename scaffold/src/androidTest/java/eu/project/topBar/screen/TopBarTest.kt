package eu.project.topBar.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.project.common.TestTags
import eu.project.topBar.model.TopBarViewState
import eu.project.ui.R
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var isBackClicked = false
    private var isInfoClicked = false

    @Before
    fun setUp() {
        isBackClicked = false
        isInfoClicked = false
    }

    private fun setContent(
        screenName: Int,
        showBackIcon: Boolean = true,
        showInfoIcon: Boolean = true
    ) {
        val viewState = TopBarViewState(
            screenName = screenName,
            showBackIcon = showBackIcon,
            showInfoIcon = showInfoIcon
        )

        composeTestRule.setContent {

            topBar(
                topBarViewState = viewState,
                onNavigateBack = { isBackClicked = true },
                onDisplayInfo = { isInfoClicked = true }
            )
        }
    }



    @Test
    fun backIconAndText_areDisplayed_whenEnabled() {
        setContent(
            screenName = R.string.saved_words,
            showBackIcon = true,
            showInfoIcon = false
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_BACK_ICON)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Saved words")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun backIconAndText_areNotDisplayed_whenDisabled() {
        setContent(
            screenName = R.string.saved_words,
            showBackIcon = false,
            showInfoIcon = false
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_BACK_ICON)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Saved words")
            .assertDoesNotExist()
    }

    @Test
    fun backIconAndText_click_triggersCallback() {
        setContent(
            screenName = R.string.saved_words,
            showBackIcon = true,
            showInfoIcon = false
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_BACK_ICON)
            .performClick()

        assertTrue(isBackClicked)
    }



    @Test
    fun infoIcon_isDisplayed_whenEnabled() {
        setContent(
            screenName = R.string.home,
            showBackIcon = false,
            showInfoIcon = true
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_INFO_ICON)
            .assertIsDisplayed()
    }

    @Test
    fun infoIcon_isNotDisplayed_whenDisabled()  {
        setContent(
            screenName = R.string.home,
            showBackIcon = false,
            showInfoIcon = false
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_INFO_ICON)
            .assertIsNotDisplayed()
    }

    @Test
    fun infoIcon_click_triggersCallback() {
        setContent(
            screenName = R.string.home,
            showBackIcon = false,
            showInfoIcon = true
        )

        composeTestRule
            .onNodeWithTag(TestTags.TOP_BAR_ICON_BUTTON_INFO_ICON)
            .performClick()

        assertTrue(isInfoClicked)
    }
}
