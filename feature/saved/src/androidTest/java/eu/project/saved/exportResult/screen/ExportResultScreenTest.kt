package eu.project.saved.exportResult.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import eu.project.common.remoteData.CsvFile
import eu.project.saved.exportResult.state.ExportResultScreenState
import eu.project.ui.R
import eu.project.ui.theme.Background
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportResultScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onClickSaveExportedWordsWasCalled = false
    private var onClickTryAgainLaterWasCalled = false
    private var onClickContinueWasCalled = false
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testCsvFile = CsvFile(data = "test,csv,data".toByteArray())
    private val testThrowable = RuntimeException("Test error message")

    private var screenState = MutableStateFlow<ExportResultScreenState>(ExportResultScreenState.Loading)

    @Before
    fun setUp() {

        onClickSaveExportedWordsWasCalled = false
        onClickTryAgainLaterWasCalled = false
        onClickContinueWasCalled = false

        composeTestRule.setContent {

            Column(Modifier.background(Background)) {

                exportResultScreen(
                    screenState = screenState.collectAsState().value,
                    onClickSaveExportedWords = { onClickSaveExportedWordsWasCalled = true },
                    onClickTryAgainLater = { onClickTryAgainLaterWasCalled = true },
                    onClickContinue = { onClickContinueWasCalled = true }
                )
            }
        }
    }

//- Loading state tests -------------------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_loadingState_displaysLoadingContent() {

        // setup
        screenState.value = ExportResultScreenState.Loading

        // test - verify loading content elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_loading_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data_explanation))
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_loadingState_screenContainerIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.Loading

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()
    }

//- ReadyToSaveFile state tests ----------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_readyToSaveFileState_displaysReadyToSaveContent() {

        // setup
        screenState.value = ExportResultScreenState.ReadyToSaveFile(csvFile = testCsvFile)

        // test - verify ready to save content elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_download_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save_explanation))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_readyToSaveFileState_saveButtonIsEnabled() {

        // setup
        screenState.value = ExportResultScreenState.ReadyToSaveFile(csvFile = testCsvFile)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsEnabled()
    }

    @Test
    fun exportResultScreen_readyToSaveFileState_saveButtonClickCallsCallback() {

        // setup
        screenState.value = ExportResultScreenState.ReadyToSaveFile(csvFile = testCsvFile)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_SAVE_EXPORTED_WORDS)
            .assertIsDisplayed()
            .performClick()

        // verify
        assertTrue(onClickSaveExportedWordsWasCalled)
    }

//- FailedToLoadFile state tests ---------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_failedToLoadFileState_displaysFailedToLoadContent() {

        // setup
        screenState.value = ExportResultScreenState.FailedToLoadFile(error = testThrowable)

        // test - verify failed to load content elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_error_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(testThrowable.message!!)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_failedToLoadFileState_tryAgainButtonIsEnabled() {

        // setup
        screenState.value = ExportResultScreenState.FailedToLoadFile(error = testThrowable)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsEnabled()
    }

    @Test
    fun exportResultScreen_failedToLoadFileState_tryAgainButtonClickCallsCallback() {

        // setup
        screenState.value = ExportResultScreenState.FailedToLoadFile(error = testThrowable)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .performClick()

        // verify
        assertTrue(onClickTryAgainLaterWasCalled)
    }

//- Screen container tests ---------------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_allStates_screenContainerIsDisplayed() {

        // test Loading state
        screenState.value = ExportResultScreenState.Loading
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // test ReadyToSaveFile state
        screenState.value = ExportResultScreenState.ReadyToSaveFile(csvFile = testCsvFile)
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // test FailedToLoadFile state
        screenState.value = ExportResultScreenState.FailedToLoadFile(error = testThrowable)
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // test SavingFile state
        screenState.value = ExportResultScreenState.SavingFile
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // test FileSavedSuccessfully state
        screenState.value = ExportResultScreenState.FileSavedSuccessfully
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // test SaveFileError state
        screenState.value = ExportResultScreenState.SaveFileError(error = testThrowable)
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()
    }

//- State-specific content tests ---------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_loadingState_onlyLoadingContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.Loading

        // test - verify loading content is displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertDoesNotExist()
    }

    @Test
    fun exportResultScreen_readyToSaveFileState_onlyReadyToSaveContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.ReadyToSaveFile(csvFile = testCsvFile)

        // test - verify ready to save content is displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertDoesNotExist()
    }

    @Test
    fun exportResultScreen_failedToLoadFileState_onlyFailedToLoadContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.FailedToLoadFile(error = testThrowable)

        // test - verify failed to load content is displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_save_file))
            .assertDoesNotExist()
    }

//- SavingFile state tests ---------------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_savingFileState_screenContainerIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.SavingFile

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_savingFileState_onlySavingFileContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.SavingFile

        // test - verify screen container is displayed (SavingFile content is empty)
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN)
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_save_file))
            .assertDoesNotExist()
    }

//- FileSavedSuccessfully state tests ----------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_fileSavedSuccessfullyState_displaysFileSavedSuccessfullyContent() {

        // setup
        screenState.value = ExportResultScreenState.FileSavedSuccessfully

        // test - verify file saved successfully content elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_confirmation_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully_explanation))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_fileSavedSuccessfullyState_continueButtonIsEnabled() {

        // setup
        screenState.value = ExportResultScreenState.FileSavedSuccessfully

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsEnabled()
    }

    @Test
    fun exportResultScreen_fileSavedSuccessfullyState_continueButtonClickCallsCallback() {

        // setup
        screenState.value = ExportResultScreenState.FileSavedSuccessfully

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_CONTINUE)
            .assertIsDisplayed()
            .performClick()

        // verify
        assertTrue(onClickContinueWasCalled)
    }

    @Test
    fun exportResultScreen_fileSavedSuccessfullyState_onlyFileSavedSuccessfullyContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.FileSavedSuccessfully

        // test - verify file saved successfully content is displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_save_file))
            .assertDoesNotExist()
    }

//- SaveFileError state tests ------------------------------------------------------------------------------------------

    @Test
    fun exportResultScreen_saveFileErrorState_displaysSaveFileErrorContent() {

        // setup
        screenState.value = ExportResultScreenState.SaveFileError(error = testThrowable)

        // test - verify save file error content elements are displayed
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.illustration_error_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_save_file))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(testThrowable.message!!)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsDisplayed()
    }

    @Test
    fun exportResultScreen_saveFileErrorState_tryAgainButtonIsEnabled() {

        // setup
        screenState.value = ExportResultScreenState.SaveFileError(error = testThrowable)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .assertIsEnabled()
    }

    @Test
    fun exportResultScreen_saveFileErrorState_tryAgainButtonClickCallsCallback() {

        // setup
        screenState.value = ExportResultScreenState.SaveFileError(error = testThrowable)

        // test
        composeTestRule
            .onNodeWithTag(TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER)
            .performClick()

        // verify
        assertTrue(onClickTryAgainLaterWasCalled)
    }

    @Test
    fun exportResultScreen_saveFileErrorState_onlySaveFileErrorContentIsDisplayed() {

        // setup
        screenState.value = ExportResultScreenState.SaveFileError(error = testThrowable)

        // test - verify save file error content is displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_save_file))
            .assertIsDisplayed()

        // verify other state content is not displayed
        composeTestRule
            .onNodeWithText(context.getString(R.string.loading_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.ready_to_save))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.failed_to_load_data))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.file_saved_successfully))
            .assertDoesNotExist()
    }
}