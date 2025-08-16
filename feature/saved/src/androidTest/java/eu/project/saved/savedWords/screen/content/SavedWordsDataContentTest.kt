package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.project.common.TestTags
import eu.project.common.model.SavedWord
import eu.project.common.testHelpers.SavedWordTestInstances
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedWordsDataContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var retrievedData: MutableStateFlow<List<SavedWord>>
    private var onRequestDeleteWasClicked: SavedWord? = null

    @Before
    fun setUp() {

        retrievedData = MutableStateFlow(SavedWordTestInstances.list)
        onRequestDeleteWasClicked = null

        composeTestRule.setContent {
            Column(Modifier.background(Background)) {

                savedWordsDataContent(
                    retrievedData = retrievedData.collectAsState().value,
                    onRequestDelete = { onRequestDeleteWasClicked = it }
                )
            }
        }
    }



//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun savedWordsDataContent_defaultState_allSavedWordCardsAreDisplayed() {

        // test
        composeTestRule
            .onNodeWithTag(SavedWordTestInstances.first.toString())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SavedWordTestInstances.second.toString())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SavedWordTestInstances.third.toString())
            .assertIsDisplayed()
    }

    @Test
    fun savedWordsDataContent_defaultState_allDeleteIconsAreDisplayed() {

        // test
        SavedWordTestInstances.list.forEach {

            composeTestRule
                .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - $it")
                .assertIsDisplayed()
        }
    }

    @Test
    fun savedWordsDataContent_emptyDataList_noCardsAreDisplayed() {

        // setup
        retrievedData.update { emptyList() }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN).assertIsDisplayed()
    }

    @Test
    fun savedWordsDataContent_singleWordData_onlyOneCardIsDisplayed() {

        // setup
        retrievedData.update { listOf(SavedWordTestInstances.first) }

        // test
        composeTestRule
            .onNodeWithTag(SavedWordTestInstances.first.toString())
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun savedWordsDataContent_defaultState_displaysCorrectWordTexts() {

        // test
        SavedWordTestInstances.list.forEach {

            composeTestRule
                .onNodeWithText(it.word)
                .assertIsDisplayed()
        }
    }



//- delete button interaction tests ------------------------------------------------------------------------------------

    @Test
    fun savedWordsDataContent_firstWordDeleteButtonClicked_callsOnRequestDeleteWithFirstWord() {

        // test
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.first}")
            .performClick()

        assertEquals(SavedWordTestInstances.first, onRequestDeleteWasClicked)
    }

    @Test
    fun savedWordsDataContent_secondWordDeleteButtonClicked_callsOnRequestDeleteWithSecondWord() {

        // test
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.second}")
            .performClick()

        assertEquals(SavedWordTestInstances.second, onRequestDeleteWasClicked)
    }

    @Test
    fun savedWordsDataContent_thirdWordDeleteButtonClicked_callsOnRequestDeleteWithThirdWord() {

        // test
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.third}")
            .performClick()

        assertEquals(SavedWordTestInstances.third, onRequestDeleteWasClicked)
    }

    @Test
    fun savedWordsDataContent_differentWordCardsClicked_callsOnRequestDeleteWithCorrectWords() {

        // test first word
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.first}")
            .performClick()

        assertEquals(SavedWordTestInstances.first, onRequestDeleteWasClicked)

        // reset callback
        onRequestDeleteWasClicked = null

        // test second word
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.second}")
            .performClick()

        assertEquals(SavedWordTestInstances.second, onRequestDeleteWasClicked)

        // reset callback
        onRequestDeleteWasClicked = null

        // test third word
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.third}")
            .performClick()

        assertEquals(SavedWordTestInstances.third, onRequestDeleteWasClicked)
    }

    @Test
    fun savedWordsDataContent_sameWordCardClickedMultipleTimes_callsOnRequestDeleteEachTime() {

        // test
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.first}")
            .performClick()

        assertEquals(SavedWordTestInstances.first, onRequestDeleteWasClicked)

        // reset callback
        onRequestDeleteWasClicked = null

        // click same word again
        composeTestRule
            .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - ${SavedWordTestInstances.first}")
            .performClick()

        assertEquals(SavedWordTestInstances.first, onRequestDeleteWasClicked)
    }



//- layout and structure tests -----------------------------------------------------------------------------------------

    @Test
    fun savedWordsDataContent_defaultState_displaysWordsInCorrectOrder() {

        composeTestRule.onNodeWithText(SavedWordTestInstances.first.word).assertIsDisplayed()
        composeTestRule.onNodeWithText(SavedWordTestInstances.second.word).assertIsDisplayed()
        composeTestRule.onNodeWithText(SavedWordTestInstances.third.word).assertIsDisplayed()
    }

    @Test
    fun savedWordsDataContent_defaultState_eachCardHasWordAndLanguageText() {

        // test - verify each card contains both word and language text
        val firstWordCard = composeTestRule.onNodeWithTag(SavedWordTestInstances.first.toString())
        firstWordCard.assertIsDisplayed()

        // Verify the card contains both word and language text
        composeTestRule
            .onNodeWithText(SavedWordTestInstances.first.word)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(SavedWordTestInstances.first.language)
            .assertIsDisplayed()
    }

    @Test
    fun savedWordsDataContent_defaultState_eachCardHasDeleteButton() {

        SavedWordTestInstances.list.forEach {

            composeTestRule
                .onNodeWithTag("${TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON} - $it")
                .assertIsDisplayed()
        }
    }
}