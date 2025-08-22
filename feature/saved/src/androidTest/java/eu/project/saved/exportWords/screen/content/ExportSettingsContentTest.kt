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
import eu.project.saved.exportWords.state.ExportSettingsUiState
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

    @Before
    fun setUp() {

        exportSettingsUiState = MutableStateFlow(ExportSettingsUiState())
        onClickSendMethodWasClicked = false
        onClickDownloadMethodWasClicked = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {
                exportSettingsContent(
                    exportSettingsUiState = exportSettingsUiState.collectAsState().value,
                    onClickSendMethod = {
                        onClickSendMethodWasClicked = true
                    },
                    onClickDownloadMethod = {
                        onClickDownloadMethodWasClicked = true
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
