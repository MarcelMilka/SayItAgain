package eu.project.saved.exportWords.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsScreenState
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.ui.SubscreenControllerButtonVariants
import eu.project.ui.R
import eu.project.ui.theme.Background
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
    private var onClickLeft = false
    private var onClickRight = false
    private var onClickSendMethod = false
    private var onClickDownloadMethod = false

    private val firstInstance = ExportableSavedWord(UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English", false)
    private val secondInstance = ExportableSavedWord(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English", false)
    private val thirdInstance = ExportableSavedWord(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian", false)
    private val exportableWords = listOf(firstInstance, secondInstance, thirdInstance)

    @Before
    fun setUp() {

        screenState = MutableStateFlow(ExportWordsScreenState.Initial)
        uiState = MutableStateFlow(ExportWordsUiState(exportableWords))

        onClickLeft = false
        onClickRight = false
        onClickSendMethod = false
        onClickDownloadMethod = false

        composeTestRule.setContent {

            Box(Modifier.background(Background)) {

                exportWordsScreen(
                    listState = LazyListState(),
                    screenState = screenState.collectAsState().value,
                    uiState = uiState.collectAsState().value,
                    onChangeWordSelection = { onChangeWordSelection = it },
                    onClickLeft = { onClickLeft = true },
                    onClickRight = { onClickRight = true },
                    onClickSendMethod = { onClickSendMethod = true },
                    onClickDownloadMethod = { onClickDownloadMethod = true },
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

    // componentUnderTest_condition_and_condition_expectedResult



//- readyToExport tests ------------------------------------------------------------------------------------------------
    @Test
    fun exportWordsScreen_screenStateReadyToExport_showReadyToExportDisplayed() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SHOW_READY_TO_EXPORT)
            .assertIsDisplayed()
    }

//- readyToExport -> subscreenController -------------------------------------------------------------------------------
    @Test
    fun subscreenController_isVisibleTrue_subscreenControllerIsVisible() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    isVisible = true,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.select_words)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.export_settings)).assertIsDisplayed()
    }

    @Test
    fun subscreenController_leftButtonActive_leftButtonIsNotEnabled() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    leftButton = SubscreenControllerButtonVariants.leftActive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_LEFT_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun subscreenController_leftButtonInactive_leftButtonIsEnabled() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    leftButton = SubscreenControllerButtonVariants.leftInactive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_LEFT_BUTTON).assertIsEnabled()
    }

    @Test
    fun subscreenController_leftButtonClick_callsLambda() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    leftButton = SubscreenControllerButtonVariants.leftInactive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_LEFT_BUTTON).performClick()
        assertTrue(onClickLeft)
    }

    @Test
    fun subscreenController_rightButtonActive_rightButtonIsNotEnabled() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    rightButton = SubscreenControllerButtonVariants.rightActive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_LEFT_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun subscreenController_rightButtonInactive_rightButtonIsEnabled() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    rightButton = SubscreenControllerButtonVariants.rightInactive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_RIGHT_BUTTON).assertIsEnabled()
    }

    @Test
    fun subscreenController_rightButtonClick_callsLambda() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update {

            it.copy(
                subscreenControllerState = it.subscreenControllerState.copy(
                    rightButton = SubscreenControllerButtonVariants.rightInactive,
                )
            )
        }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_RIGHT_BUTTON).performClick()
        assertTrue(onClickRight)
    }

//- readyToExport -> warningBanner -------------------------------------------------------------------------------------
    @Test
    fun showNoWordsSelectedBannerIsTrue_warningBannerIsDisplayed() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update { it.copy(showNoWordsSelectedBanner = true) }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER).assertIsDisplayed()
    }

    @Test
    fun showNoWordsSelectedBannerIsFalse_warningBannerIsNotDisplayed() {

        // set up
        screenState.value = ExportWordsScreenState.ReadyToExport
        uiState.update { it.copy(showNoWordsSelectedBanner = false) }

        // test
        composeTestRule.onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER).assertIsNotDisplayed()
    }

//- readyToExport -> exportMethodSelector ------------------------------------------------------------------------------

}