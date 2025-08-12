package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.common.testHelpers.SavedWordTestInstances
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.model.convertToExportable
import eu.project.saved.exportWords.state.SelectWordsUiState
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectWordsContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var selectWordsUiState: MutableStateFlow<SelectWordsUiState>
    private lateinit var wordsToExport: List<ExportableSavedWord>
    private var onChangeWordSelectionWasClicked: ExportableSavedWord? = null

    @Before
    fun setUp() {
        selectWordsUiState = MutableStateFlow(SelectWordsUiState())
        wordsToExport = listOf(
            SavedWordTestInstances.first.convertToExportable(),
            SavedWordTestInstances.second.convertToExportable(),
            SavedWordTestInstances.third.convertToExportable()
        )
        onChangeWordSelectionWasClicked = null

        composeTestRule.setContent {
            Column(Modifier.background(Background)) {
                selectWordsContent(
                    selectWordsUiState = selectWordsUiState.collectAsState().value,
                    wordsToExport = wordsToExport,
                    onChangeWordSelection = {
                        onChangeWordSelectionWasClicked = it
                    }
                )
            }
        }
    }



//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun selectWordsContent_defaultState_lazyColumnIsDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN)
            .assertIsDisplayed()
    }

    @Test
    fun selectWordsContent_defaultState_warningBannerIsNotDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER)
            .assertIsNotDisplayed()
    }

    @Test
    fun selectWordsContent_showNoWordsSelectedBannerTrue_warningBannerIsDisplayed() {

        selectWordsUiState.value = SelectWordsUiState(showNoWordsSelectedBanner = true)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER)
            .assertIsDisplayed()
    }

    @Test
    fun selectWordsContent_showNoWordsSelectedBannerFalse_warningBannerIsNotDisplayed() {

        selectWordsUiState.value = SelectWordsUiState(showNoWordsSelectedBanner = false)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER)
            .assertIsNotDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun selectWordsContent_showNoWordsSelectedBannerTrue_displaysCorrectWarningTexts() {

        selectWordsUiState.value = SelectWordsUiState(showNoWordsSelectedBanner = true)

        composeTestRule
            .onNodeWithText(context.getString(R.string.no_words_selected))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.please_select_at_least_one_word_before_continuing))
            .assertIsDisplayed()
    }



//- word card interaction tests ----------------------------------------------------------------------------------------

    @Test
    fun selectWordsContent_wordCardClicked_callsOnChangeWordSelection() {

        val firstWord = wordsToExport.first()

        composeTestRule
            .onNodeWithTag(firstWord.word)
            .performClick()

        assertEquals(firstWord, onChangeWordSelectionWasClicked)
    }

    @Test
    fun selectWordsContent_differentWordCardClicked_callsOnChangeWordSelectionWithCorrectWord() {

        val secondWord = wordsToExport[1]

        composeTestRule
            .onNodeWithTag(secondWord.word)
            .performClick()

        assertEquals(secondWord, onChangeWordSelectionWasClicked)
    }

    @Test
    fun selectWordsContent_wordCardClickedMultipleTimes_callsOnChangeWordSelectionEachTime() {

        val firstWord = wordsToExport.first()
        val secondWord = wordsToExport[1]

        // Click first word
        composeTestRule
            .onNodeWithTag(firstWord.word)
            .performClick()

        assertEquals(firstWord, onChangeWordSelectionWasClicked)

        // Reset the callback result
        onChangeWordSelectionWasClicked = null

        // Click second word
        composeTestRule
            .onNodeWithTag(secondWord.word)
            .performClick()

        assertEquals(secondWord, onChangeWordSelectionWasClicked)
    }



//- animation wrapper tests --------------------------------------------------------------------------------------------

    @Test
    fun selectWordsContent_animatedVisibilityWrapper_visibleStateShowsContent() {

        selectWordsUiState.value = SelectWordsUiState(showNoWordsSelectedBanner = true)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER)
            .assertIsDisplayed()
    }

    @Test
    fun selectWordsContent_animatedVisibilityWrapper_hiddenStateHidesContent() {

        selectWordsUiState.value = SelectWordsUiState(showNoWordsSelectedBanner = false)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER)
            .assertIsNotDisplayed()
    }
}