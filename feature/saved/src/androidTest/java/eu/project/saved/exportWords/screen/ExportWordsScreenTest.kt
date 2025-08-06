package eu.project.saved.exportWords.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsScreenState
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

internal class ExportWordsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext


    private lateinit var screenState: MutableStateFlow<ExportWordsScreenState>
    private lateinit var uiState: MutableStateFlow<ExportWordsUiState>
    private var onChangeWordSelection: ExportableSavedWord? = null

    private val firstInstance = ExportableSavedWord(UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English", false)
    private val secondInstance = ExportableSavedWord(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English", false)
    private val thirdInstance = ExportableSavedWord(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian", false)
    private val exportableWords = listOf(firstInstance, secondInstance, thirdInstance)

    @Before
    fun setUp() {

        screenState = MutableStateFlow(ExportWordsScreenState.Initial)
        uiState = MutableStateFlow(ExportWordsUiState(exportableWords))

        composeTestRule.setContent {

            Box(Modifier.background(Background)) {

                exportWordsScreen(
                    listState = LazyListState(),
                    screenState = screenState.collectAsState().value,
                    uiState = uiState.collectAsState().value,
                    onChangeWordSelection = { onChangeWordSelection = it },
                )
            }
        }
    }



    @Test
    fun stateInitial_showError() {

        // test
        composeTestRule.onNodeWithText("ShowError").assertIsDisplayed()
    }

    @Test
    fun stateError_showError() {

        // set up
        screenState.value = ExportWordsScreenState.Error

        // test
        composeTestRule.onNodeWithText("ShowError").assertIsDisplayed()
    }

    @Test
    fun stateDisconnected_showDisconnected() {

        // set up
        screenState.value = ExportWordsScreenState.Disconnected

        // test
        composeTestRule.onNodeWithText("ShowDisconnected").assertIsDisplayed()
    }


    // readyToExport tests
    @Test
    fun stateReadyToExport_showReadyToExport() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN).assertIsDisplayed()
        exportableWords.forEach { exportableWord ->

            composeTestRule.onNodeWithTag(exportableWord.uuid.toString()).assertIsDisplayed()
        }
    }
}