package eu.project.saved.exportWords.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import eu.project.common.TestTags
import eu.project.saved.exportWords.state.SubscreenControllerState
import eu.project.saved.exportWords.state.SubscreenControllerButtonVariants
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubscreenControllerTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var subscreenControllerState: MutableStateFlow<SubscreenControllerState>
    private var onSwitchToSelectWordsWasClicked = false
    private var onTryToSwitchToExportSettingsWasClicked = false

    @Before
    fun setUp() {
        onSwitchToSelectWordsWasClicked = false
        onTryToSwitchToExportSettingsWasClicked = false
        
        subscreenControllerState = MutableStateFlow(SubscreenControllerState())

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {
                subscreenController(
                    subscreenControllerState = subscreenControllerState.collectAsState().value,
                    onSwitchToSelectWords = { onSwitchToSelectWordsWasClicked = true },
                    onTryToSwitchToExportSettings = { onTryToSwitchToExportSettingsWasClicked = true }
                )
            }
        }
    }



//- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun subscreenController_defaultState_subscreenControllerIsDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER)
            .assertIsDisplayed()
    }

    @Test
    fun subscreenController_defaultState_bothButtonsAreDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON)
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON)
            .assertIsDisplayed()
    }



//- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun subscreenController_defaultState_displaysCorrectButtonTexts() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.select_words))
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText(context.getString(R.string.export_settings))
            .assertIsDisplayed()
    }



//- 'Select words' button tests ----------------------------------------------------------------------------------------

    @Test
    fun subscreenController_selectWordsButtonDisabled_selectWordsButtonIsNotEnabled() {

        subscreenControllerState.value = SubscreenControllerState(
            selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsDisabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun subscreenController_selectWordsButtonEnabled_selectWordsButtonIsEnabled() {

        subscreenControllerState.value = SubscreenControllerState(
            selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsEnabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun subscreenController_selectWordsButtonEnabledAndClicked_callsOnSwitchToSelectWords() {

        subscreenControllerState.value = SubscreenControllerState(
            selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsEnabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON)
            .performClick()

        assertTrue(onSwitchToSelectWordsWasClicked)
    }

    @Test
    fun subscreenController_selectWordsButtonDisabledAndClicked_doesNotCallOnSwitchToSelectWords() {

        subscreenControllerState.value = SubscreenControllerState(
            selectWordsButtonState = SubscreenControllerButtonVariants.selectWordsDisabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_SELECT_WORDS_BUTTON)
            .performClick()

        assertFalse(onSwitchToSelectWordsWasClicked)
    }



//- 'Export settings' button tests -------------------------------------------------------------------------------------

    @Test
    fun subscreenController_exportSettingsButtonDisabled_exportSettingsButtonIsNotEnabled() {

        subscreenControllerState.value = SubscreenControllerState(
            exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsDisabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun subscreenController_exportSettingsButtonEnabled_exportSettingsButtonIsEnabled() {

        subscreenControllerState.value = SubscreenControllerState(
            exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsEnabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun subscreenController_exportSettingsButtonEnabledAndClicked_callsOnTryToSwitchToExportSettings() {

        subscreenControllerState.value = SubscreenControllerState(
            exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsEnabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON)
            .performClick()

        assertTrue(onTryToSwitchToExportSettingsWasClicked)
    }

    @Test
    fun subscreenController_exportSettingsButtonDisabledAndClicked_doesNotCallOnTryToSwitchToExportSettings() {

        subscreenControllerState.value = SubscreenControllerState(
            exportSettingsButtonState = SubscreenControllerButtonVariants.exportSettingsDisabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_SUBSCREEN_CONTROLLER_EXPORT_SETTINGS_BUTTON)
            .performClick()

        assertFalse(onTryToSwitchToExportSettingsWasClicked)
    }
}



//----------------------------------------------------------------------------------------------------------------------