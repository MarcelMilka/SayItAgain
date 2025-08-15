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
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import eu.project.ui.R
import eu.project.ui.theme.Background
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

internal class SavedWordsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var onRequestDelete: SavedWord? = null
    private var onDelete: SavedWord? = null
    private var onCancel = false
    private var onNavigateSelectAudioScreen = false

    private lateinit var viewState: MutableStateFlow<SavedWordsScreenState>
    private lateinit var dialogViewState: MutableStateFlow<DiscardWordDialogState>

    private val firstInstance = SavedWord(
        uuid = UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"),
        word = "Cat",
        language = "English"
    )
    private val secondInstance = SavedWord(
        uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        word = "Monitor lizard",
        language = "English"
    )
    private val thirdInstance = SavedWord(
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        word = "Hagetreboa",
        language = "Norwegian"
    )

    @Before
    fun setUp() {

        onRequestDelete = null
        onDelete = null
        onCancel = false
        onNavigateSelectAudioScreen = false

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



    @Test
    fun savedWordsScreen_displaysLoadingComponent_whenStateIsLoading() {

        // stub
        viewState.value = SavedWordsScreenState.Loading

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_displaysNoDataComponent_whenStateIsNoData() {

        // stub
        viewState.value = SavedWordsScreenState.Loaded.NoData

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // illustration and text
            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.once_you_save_your_first_word_it_will_show_up_here)).assertIsDisplayed()

            // button "Pick and transcribe"
            composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_displaysDataComponent_whenStateIsLoadedWithData() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // saved words are displayed
            composeTestRule.onNodeWithTag(firstInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(secondInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(thirdInstance.toString()).assertIsDisplayed()

            // every savedWordCard's icon button is clickable
            composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - $firstInstance").assertIsEnabled()
            composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - $secondInstance").assertIsEnabled()
            composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - $thirdInstance").assertIsEnabled()
    }

    @Test
    fun savedWordsScreen_displaysErrorComponent_whenStateIsFailedToLoad() {

        // stub
        viewState.value = SavedWordsScreenState.Error("Exemplary error cause")

        // test
            composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // illustration and text
            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.something_is_off)).assertIsDisplayed()
            composeTestRule.onNodeWithText("Exemplary error cause").assertIsDisplayed()
    }



    @Test
    fun savedWordsScreen_switchesFromLoadingToNoData() {

        // stub
        viewState.value = SavedWordsScreenState.Loading

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // Loading
            composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

            // NoData
            viewState.value = SavedWordsScreenState.Loaded.NoData

            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.once_you_save_your_first_word_it_will_show_up_here)).assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
            composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsDisplayed()
            composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsEnabled()
    }

    @Test
    fun savedWordsScreen_switchesFromLoadingToData() {

        // stub
        viewState.value = SavedWordsScreenState.Loading

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // Loading
            composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

            // Data
            val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
            viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)

            composeTestRule.onNodeWithTag(firstInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(secondInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(thirdInstance.toString()).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_switchesFromLoadingToFailedToLoad() {

        // stub
        viewState.value = SavedWordsScreenState.Loading

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN).assertIsDisplayed()

            // Loading
            composeTestRule.onNodeWithText(context.getString(R.string.loading)).assertIsDisplayed()

            // FailedToLoad
            viewState.value = SavedWordsScreenState.Error("Exemplary error cause")

            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.something_is_off)).assertIsDisplayed()
            composeTestRule.onNodeWithText("Exemplary error cause").assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_switchesFromNoDataToData() {

        // stub
        viewState.value = SavedWordsScreenState.Loaded.NoData

        // test

            // NoData
            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_empty_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.kinda_empty)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.once_you_save_your_first_word_it_will_show_up_here)).assertIsDisplayed()

            composeTestRule.onNodeWithText(context.getString(R.string.pick_and_transcribe)).assertIsDisplayed()
            composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsDisplayed()
            composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).assertIsEnabled()

            // Data
            val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
            viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)

            composeTestRule.onNodeWithTag(firstInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(secondInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(thirdInstance.toString()).assertIsDisplayed()
    }

    @Test
    fun savedWordsScreen_switchesFromDataToFailedToLoad() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)

        // test

            // Data
            composeTestRule.onNodeWithTag(firstInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(secondInstance.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithTag(thirdInstance.toString()).assertIsDisplayed()

            // FailedToLoad
            viewState.value = SavedWordsScreenState.Error("Exemplary error cause")

            composeTestRule.onNodeWithContentDescription(context.getString(R.string.illustration_error_description)).assertIsDisplayed()
            composeTestRule.onNodeWithText(context.getString(R.string.something_is_off)).assertIsDisplayed()
            composeTestRule.onNodeWithText("Exemplary error cause").assertIsDisplayed()

    }



    @Test
    fun savedWordsScreen_calls_onRequestDelete() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)

        // test
        composeTestRule.onNodeWithContentDescription("${context.getString(R.string.delete)} - $secondInstance").performClick()
        assertEquals(secondInstance, onRequestDelete)
    }

    @Test
    fun savedWordsScreen_doesNotDisplayDiscardWordDialog_whenDialogViewStateHidden() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)
        dialogViewState.value = DiscardWordDialogState.Hidden

        // test
        composeTestRule.onNodeWithTag(TestTags.DISCARD_WORD_DIALOG).assertDoesNotExist()

        composeTestRule.onNodeWithText(context.getString(R.string.discard_the_word)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.the_word)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.will_be_deleted_forever)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.will_be_deleted_forever)).assertDoesNotExist()

        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).assertDoesNotExist()
    }

    @Test
    fun savedWordsScreen_displaysDiscardWordDialog_whenDialogViewStateIsVisible() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)
        dialogViewState.value = DiscardWordDialogState.Visible(firstInstance)

        // test

        composeTestRule.onNodeWithTag(TestTags.DISCARD_WORD_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.discard_the_word)).assertIsDisplayed()

        val theWord = context.getString(R.string.the_word)
        val willBe = context.getString(R.string.will_be_deleted_forever)
        composeTestRule.onNodeWithText("$theWord '${firstInstance.word}' $willBe").assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).assertIsDisplayed()
    }

    @Test
    fun discardWordDialog_calls_onDelete() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)
        dialogViewState.value = DiscardWordDialogState.Visible(firstInstance)

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON).performClick()

        assertEquals(firstInstance, onDelete)
    }

    @Test
    fun discardWordDialog_calls_onCancel() {

        // stub
        val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)
        viewState.value = SavedWordsScreenState.Loaded.Data(retrievedData)
        dialogViewState.value = DiscardWordDialogState.Visible(firstInstance)

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON).performClick()

        assertTrue(onCancel)
    }

    @Test
    fun savedWordsScreen_calls_onNavigateSelectAudioScreen() {

        // stub
        viewState.value = SavedWordsScreenState.Loaded.NoData

        // test
        composeTestRule.onNodeWithTag(TestTags.SAVED_WORDS_SCREEN_BUTTON_PICK_AND_TRANSCRIBE).performClick()
        assertTrue(onNavigateSelectAudioScreen)
    }
}