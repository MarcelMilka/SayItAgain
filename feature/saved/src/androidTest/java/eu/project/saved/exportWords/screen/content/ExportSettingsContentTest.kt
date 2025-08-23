package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import eu.project.saved.exportWords.state.ExportSettingsUiState
import eu.project.saved.exportWords.state.ExportWordsButtonVariants
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportSettingsContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var exportSettingsUiState: MutableStateFlow<ExportSettingsUiState>
    private var onClickSendMethodWasClicked: Boolean = false
    private var onClickDownloadMethodWasClicked: Boolean = false
    private var onClickExportWordsWasClicked: Boolean = false

    @Before
    fun setUp() {

        exportSettingsUiState = MutableStateFlow(ExportSettingsUiState())
        onClickSendMethodWasClicked = false
        onClickDownloadMethodWasClicked = false
        onClickExportWordsWasClicked = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {
                exportSettingsContent(
                    exportSettingsUiState = exportSettingsUiState.collectAsState().value,
                    onClickSendMethod = {
                        onClickSendMethodWasClicked = true
                    },
                    onClickDownloadMethod = {
                        onClickDownloadMethodWasClicked = true
                    },
                    onClickExportWords = {
                        onClickExportWordsWasClicked = true
                    }
                )
            }
        }
    }

    //- visibility tests ---------------------------------------------------------------------------------------------------

    @Test
    fun exportSettingsContent_defaultState_warningBannerIsNotDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER)
            .assertIsNotDisplayed()
    }

    @Test
    fun exportSettingsContent_showExportMethodNotAvailableBannerTrue_warningBannerIsDisplayed() {

        exportSettingsUiState.value = ExportSettingsUiState(showExportMethodNotAvailableBanner = true)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER)
            .assertIsDisplayed()
    }

    @Test
    fun exportSettingsContent_showExportMethodNotAvailableBannerFalse_warningBannerIsNotDisplayed() {

        exportSettingsUiState.value = ExportSettingsUiState(showExportMethodNotAvailableBanner = false)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER)
            .assertIsNotDisplayed()
    }

    //- text display tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportSettingsContent_defaultState_displaysCorrectHeadlineText() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.how_would_you_like_to_export_your_words))
            .assertIsDisplayed()
    }

    @Test
    fun exportSettingsContent_showExportMethodNotAvailableBannerTrue_displaysCorrectWarningTexts() {

        exportSettingsUiState.value = ExportSettingsUiState(showExportMethodNotAvailableBanner = true)

        composeTestRule
            .onNodeWithText(context.getString(R.string.export_method_not_available))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.export_method_not_available_explanation))
            .assertIsDisplayed()
    }

    //- export method interaction tests ------------------------------------------------------------------------------------

    @Test
    fun exportSettingsContent_sendMethodClicked_callsOnClickSendMethod() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.send_to_email))
            .performClick()

        assertTrue(onClickSendMethodWasClicked)
    }

    @Test
    fun exportSettingsContent_downloadMethodClicked_callsOnClickDownloadMethod() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.download_to_device))
            .performClick()

        assertTrue(onClickDownloadMethodWasClicked)
    }

    @Test
    fun exportSettingsContent_sendMethodClickedMultipleTimes_callsOnClickSendMethodEachTime() {

        // Click send method
        composeTestRule
            .onNodeWithText(context.getString(R.string.send_to_email))
            .performClick()

        assertTrue(onClickSendMethodWasClicked)

        // Reset the callback result
        onClickSendMethodWasClicked = false

        // Click send method again
        composeTestRule
            .onNodeWithText(context.getString(R.string.send_to_email))
            .performClick()

        assertTrue(onClickSendMethodWasClicked)
    }

    @Test
    fun exportSettingsContent_downloadMethodClickedMultipleTimes_callsOnClickDownloadMethodEachTime() {

        // Click download method
        composeTestRule
            .onNodeWithText(context.getString(R.string.download_to_device))
            .performClick()

        assertTrue(onClickDownloadMethodWasClicked)

        // Reset the callback result
        onClickDownloadMethodWasClicked = false

        // Click download method again
        composeTestRule
            .onNodeWithText(context.getString(R.string.download_to_device))
            .performClick()

        assertTrue(onClickDownloadMethodWasClicked)
    }

    //- export words button tests ------------------------------------------------------------------------------------------

    @Test
    fun exportSettingsContent_defaultState_exportWordsButtonIsDisplayed() {

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .assertIsDisplayed()
    }

    @Test
    fun exportSettingsContent_defaultState_exportWordsButtonDisplaysCorrectText() {

        composeTestRule
            .onNodeWithText(context.getString(R.string.export_words_button))
            .assertIsDisplayed()
    }

    @Test
    fun exportSettingsContent_defaultState_exportWordsButtonIsDisabled() {

        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.disabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .assertIsNotEnabled()
    }

    @Test
    fun exportSettingsContent_enabledButtonState_exportWordsButtonIsEnabled() {

        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.enabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun exportSettingsContent_enabledButtonState_exportWordsButtonClicked_callsOnClickExportWords() {

        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.enabled
        )

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .performClick()

        assertTrue(onClickExportWordsWasClicked)
    }

    @Test
    fun exportSettingsContent_enabledButtonState_exportWordsButtonClickedMultipleTimes_callsOnClickExportWordsEachTime() {

        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.enabled
        )

        // Click export words button
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .performClick()

        assertTrue(onClickExportWordsWasClicked)

        // Reset the callback result
        onClickExportWordsWasClicked = false

        // Click export words button again
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .performClick()

        assertTrue(onClickExportWordsWasClicked)
    }

    @Test
    fun exportSettingsContent_buttonStateChangedFromDisabledToEnabled_exportWordsButtonBecomesClickable() {

        // Start with disabled button
        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.disabled
        )

        // Change to enabled button
        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.enabled
        )

        // Click the button
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .performClick()

        assertTrue(onClickExportWordsWasClicked)
    }

    @Test
    fun exportSettingsContent_buttonStateChangedFromEnabledToDisabled_exportWordsButtonBecomesUnclickable() {

        // Start with enabled button
        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.enabled
        )

        // Change to disabled button
        exportSettingsUiState.value = ExportSettingsUiState(
            exportWordsButtonState = ExportWordsButtonVariants.disabled
        )

        // The button should still be displayed but not clickable
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_EXPORT_WORDS_BUTTON)
            .assertIsNotEnabled()
    }

    //- animation wrapper tests --------------------------------------------------------------------------------------------

    @Test
    fun exportSettingsContent_animatedVisibilityWrapper_visibleStateShowsContent() {

        exportSettingsUiState.value = ExportSettingsUiState(showExportMethodNotAvailableBanner = true)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER)
            .assertIsDisplayed()
    }

    @Test
    fun exportSettingsContent_animatedVisibilityWrapper_hiddenStateHidesContent() {

        exportSettingsUiState.value = ExportSettingsUiState(showExportMethodNotAvailableBanner = false)

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER)
            .assertIsNotDisplayed()
    }
}

