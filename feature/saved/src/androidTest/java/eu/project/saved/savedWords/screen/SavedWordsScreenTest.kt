package eu.project.saved.savedWords.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
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
import eu.project.common.model.SavedWord
import eu.project.common.testHelpers.SavedWordTestInstances
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import eu.project.ui.R
import eu.project.ui.theme.Background
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedWordsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var onRequestDelete: SavedWord? = null
    private var onDelete: SavedWord? = null
    private var onCancel = false
    private var onNavigateSelectAudioScreen = false

    private lateinit var viewState: MutableStateFlow<SavedWordsScreenState>
    private lateinit var dialogViewState: MutableStateFlow<DiscardWordDialogState>

    private val data = SavedWordTestInstances.list
    private val swInstances = SavedWordTestInstances

    @Before
    fun setup() {

        onRequestDelete = null
        onDelete = null
        onCancel = false
        onNavigateSelectAudioScreen = false

        // initialize state flows
        viewState = MutableStateFlow(SavedWordsScreenState.Loading)
        dialogViewState = MutableStateFlow(DiscardWordDialogState.Hidden)

        composeTestRule.setContent {

            Box(Modifier.background(Background)) {

                savedWordsScreen(
                    screenState = viewState.collectAsState().value,
                    dialogState = dialogViewState.collectAsState().value,
                    onRequestDelete = { onRequestDelete = it },
                    onDelete = { onDelete = it },
                    onCancel = { onCancel = true },
                    onNavigateSelectAudioScreen = { onNavigateSelectAudioScreen = true }
                )
            }
        }
    }



//- screen state visibility tests --------------------------------------------------------------------------------------

    @Test
    fun savedWordsScreen_loadingState_showsLoadingComponent() {

        // setup
        viewState.update { SavedWordsScreenState.Loading }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_noDataState_showsNoDataComponent() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.NoData }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

        // illustration and text
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty_explanation)).assertIsDisplayed()

        // button "Pick and transcribe"
        composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_dataState_showsDataComponentWithAllWords() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

        // saved words are displayed
        composeTestRule.onNodeWithTag(swInstances.first.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.second.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.third.toString()).assertIsDisplayed()

        // every savedWordCard's icon button is clickable
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.first}").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.second}").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.third}").assertIsEnabled()
    }

    @Test
    fun savedWordsScreen_errorState_showsErrorComponentWithCorrectMessage() {

        // setup
        val errorMessage = "Exemplary error cause"
        viewState.update { SavedWordsScreenState.Error(errorMessage) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

        // illustration and text
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.something_went_wrong)).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }



//- screen state transition tests --------------------------------------------------------------------------------------

    @Test
    fun savedWordsScreen_transitionsFromLoadingToNoData_correctly() {

        // setup
        viewState.update { SavedWordsScreenState.Loading }

        // test initial loading state
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

        // transition to no data
        viewState.update { SavedWordsScreenState.Loaded.NoData }

        // verify no data state
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty_explanation)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsEnabled()
    }

    @Test
    fun savedWordsScreen_transitionsFromLoadingToData_correctly() {

        // setup
        viewState.update { SavedWordsScreenState.Loading }

        // test initial loading state
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

        // transition to data
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // verify data state
        composeTestRule.onNodeWithTag(swInstances.first.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.second.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.third.toString()).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_transitionsFromLoadingToError_correctly() {

        // setup
        viewState.update { SavedWordsScreenState.Loading }

        // test initial loading state
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

        // transition to error
        val errorMessage = "Exemplary error cause"
        viewState.update { SavedWordsScreenState.Error(errorMessage) }

        // verify error state
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.something_went_wrong)).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_transitionsFromNoDataToData_correctly() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.NoData }

        // test initial no data state
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty_explanation)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsEnabled()

        // transition to data
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // verify data state
        composeTestRule.onNodeWithTag(swInstances.first.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.second.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.third.toString()).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_transitionsFromDataToError_correctly() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // test initial data state
        composeTestRule.onNodeWithTag(swInstances.first.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.second.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.third.toString()).assertIsDisplayed()

        // transition to error
        val errorMessage = "Exemplary error cause"
        viewState.update { SavedWordsScreenState.Error(errorMessage) }

        // verify error state
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.something_went_wrong)).assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }



//- user interaction tests ---------------------------------------------------------------------------------------------

    @Test
    fun savedWordsScreen_deleteButtonClick_callsOnRequestDeleteWithCorrectWord() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // test
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.second}").performClick()
        assertEquals(swInstances.second, onRequestDelete)
    }

    @Test
    fun savedWordsScreen_pickAndTranscribeButtonClick_callsOnNavigateSelectAudioScreen() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.NoData }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).performClick()
        assertTrue(onNavigateSelectAudioScreen)
    }



//- dialog visibility tests --------------------------------------------------------------------------------------------

    @Test
    fun savedWordsScreen_hiddenDialogState_doesNotDisplayDiscardWordDialog() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }
        dialogViewState.update { DiscardWordDialogState.Hidden }

        // test
        composeTestRule.onNodeWithTag(TestTags.DISCARD_WORD_DIALOG).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.discard_the_word)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.the_word)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.will_be_deleted_forever)).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).assertDoesNotExist()
    }

    @Test
    fun savedWordsScreen_visibleDialogState_displaysDiscardWordDialogCorrectly() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }
        dialogViewState.update { DiscardWordDialogState.Visible(swInstances.first) }

        // test
        composeTestRule.onNodeWithTag(TestTags.DISCARD_WORD_DIALOG).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.discard_the_word)).assertIsDisplayed()

        val theWord = context.getString(R.string.the_word)
        val willBe = context.getString(R.string.will_be_deleted_forever)
        composeTestRule.onNodeWithText("$theWord '${swInstances.first.word}' $willBe").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).assertIsDisplayed()
    }



//- dialog interaction tests -------------------------------------------------------------------------------------------

    @Test
    fun discardWordDialog_deleteButtonClick_callsOnDeleteWithCorrectWord() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }
        dialogViewState.update { DiscardWordDialogState.Visible(swInstances.first) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).performClick()
        assertEquals(swInstances.first, onDelete)
    }

    @Test
    fun discardWordDialog_cancelButtonClick_callsOnCancel() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }
        dialogViewState.update { DiscardWordDialogState.Visible(swInstances.first) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).performClick()
        assertTrue(onCancel)
    }



//- edge case tests ---------------------------------------------------------------------------------------------------

    @Test
    fun savedWordsScreen_emptyDataList_displaysCorrectly() {

        // setup
        val emptyData = emptyList<SavedWord>()
        viewState.update { SavedWordsScreenState.Loaded.Data(emptyData) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        // Should not crash and should not display any word cards
    }

    @Test
    fun savedWordsScreen_singleWordData_displaysCorrectly() {

        // setup
        val singleWordData = listOf(swInstances.first)
        viewState.update { SavedWordsScreenState.Loaded.Data(singleWordData) }

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(swInstances.first.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.first}").assertIsEnabled()
    }

    @Test
    fun savedWordsScreen_multipleDeleteRequests_workCorrectly() {

        // setup
        viewState.update { SavedWordsScreenState.Loaded.Data(data) }

        // test first word delete request
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.first}").performClick()
        assertEquals(swInstances.first, onRequestDelete)

        // reset callback
        onRequestDelete = null

        // test second word delete request
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - ${swInstances.second}").performClick()
        assertEquals(swInstances.second, onRequestDelete)
    }
}