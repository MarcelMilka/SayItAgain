package eu.project.saved.exportResult.vm

import app.cash.turbine.test
import eu.project.common.eventBus.saveFile.SaveFileEvent
import eu.project.common.eventBus.saveFile.SaveFileEventBus
import eu.project.common.model.decodeToExportSettings
import eu.project.common.remoteData.CsvFile
import eu.project.common.remoteData.ExportRepository
import eu.project.saved.exportResult.state.ExportResultScreenState
import eu.project.saved.exportWords.vm.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportResultViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // dependencies
    private val exportRepository = mockk<ExportRepository>(relaxed = true)
    private val saveFileEventBus = mockk<SaveFileEventBus>(relaxed = true)
    private var eventBusFlow = MutableSharedFlow<SaveFileEvent>()

    // tested class
    private lateinit var viewModel: ExportResultViewModel

    private val exportSettingsSerialized = """{"wordsToExport":[{"value":"lorem"},{"value":"ipsum"},{"value":"dolor"}]}"""
    private val exportSettings = exportSettingsSerialized.decodeToExportSettings()
    private val testCsvFile = CsvFile("test,data".toByteArray())
    private val testException = RuntimeException("Test error")

    @Before
    fun setup() {

        eventBusFlow = MutableSharedFlow<SaveFileEvent>()
        every { saveFileEventBus.events } returns eventBusFlow

        viewModel = ExportResultViewModel(
            exportRepository,
            saveFileEventBus
        )
    }



//- evaluateScreenState tests ------------------------------------------------------------------------------------------

    @Test
    fun `screenState is Loading by default`() = runTest {

        viewModel.screenState.test {

            assertEquals(ExportResultScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Idle event does not change screen state when already Loading`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // Emit Idle event
            eventBusFlow.emit(SaveFileEvent.Idle)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SaveFile event updates screen state to SavingFile`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            eventBusFlow.emit(SaveFileEvent.SaveFile(testCsvFile))

            assertEquals(ExportResultScreenState.SavingFile, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `FileSavedSuccessfully event updates screen state to FileSavedSuccessfully`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // Emit FileSavedSuccessfully event
            eventBusFlow.emit(SaveFileEvent.FileSavedSuccessfully)

            assertEquals(ExportResultScreenState.FileSavedSuccessfully, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SaveFileError event updates screen state to SaveFileError`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // Emit SaveFileError event
            eventBusFlow.emit(SaveFileEvent.SaveFileError(testException))

            assertEquals(ExportResultScreenState.SaveFileError(testException), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `multiple events update screen state in correct order`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            eventBusFlow.emit(SaveFileEvent.SaveFile(testCsvFile))
            assertEquals(ExportResultScreenState.SavingFile, awaitItem())

            eventBusFlow.emit(SaveFileEvent.FileSavedSuccessfully)
            assertEquals(ExportResultScreenState.FileSavedSuccessfully, awaitItem())

            eventBusFlow.emit(SaveFileEvent.SaveFileError(testException))
            assertEquals(ExportResultScreenState.SaveFileError(testException), awaitItem())

            eventBusFlow.emit(SaveFileEvent.Idle)
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- retrieveExportSettings tests ---------------------------------------------------------------------------------------

    @Test
    fun `retrieveExportSettings triggers CSV download when valid settings provided`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        // call
        viewModel.retrieveExportSettings(exportSettingsSerialized)

        // verify
        coVerify(exactly = 1) { exportRepository.requestDownloadToDevice(any()) }
    }

    @Test
    fun `multiple retrieveExportSettings calls with valid data only triggers download once`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        // call
        viewModel.retrieveExportSettings(exportSettingsSerialized)
        viewModel.retrieveExportSettings(exportSettingsSerialized)
        viewModel.retrieveExportSettings(exportSettingsSerialized)

        // verify
        coVerify(exactly = 1) { exportRepository.requestDownloadToDevice(any()) }
    }

    @Test
    fun `retrieveExportSettings sets screenState to FailedToLoadFile when invalid JSON provided`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // call with empty string (invalid JSON)
            viewModel.retrieveExportSettings("")

            assertTrue(awaitItem() is ExportResultScreenState.FailedToLoadFile)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `CSV download is not triggered when invalid JSON provided`() = runTest {

        // call
        viewModel.retrieveExportSettings("")

        // verify
        coVerify(exactly = 0) { exportRepository.requestDownloadToDevice(any()) }
    }



//- tryToDownloadCsv tests -----------------------------------------------------------------------------------------------

    @Test
    fun `CSV download success sets screenState to ReadyToSaveFile`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        viewModel.screenState.test {

            skipItems(1)

            // call
            viewModel.retrieveExportSettings(exportSettingsSerialized)

            assertEquals(ExportResultScreenState.ReadyToSaveFile(testCsvFile), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `CSV download failure sets screenState to FailedToLoadFile`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.failure(testException)

        viewModel.screenState.test {

            skipItems(1)

            // call
            viewModel.retrieveExportSettings(exportSettingsSerialized)

            assertEquals(ExportResultScreenState.FailedToLoadFile(testException), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `CSV download is called with correct export settings`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        // call
        viewModel.retrieveExportSettings(exportSettingsSerialized)

        // verify
        coVerify(exactly = 1) { exportRepository.requestDownloadToDevice(exportSettings.wordsToExport) }
    }



//- integration tests ----------------------------------------------------------------------------------------------------

    @Test
    fun `complete flow from retrieveExportSettings to ReadyToSaveFile works correctly`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // call
            viewModel.retrieveExportSettings(exportSettingsSerialized)

            // Should transition to ReadyToSaveFile
            assertEquals(ExportResultScreenState.ReadyToSaveFile(testCsvFile), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `complete flow from retrieveExportSettings to FailedToLoadFile works correctly`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.failure(testException)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // call
            viewModel.retrieveExportSettings(exportSettingsSerialized)

            // Should transition to FailedToLoadFile
            assertEquals(ExportResultScreenState.FailedToLoadFile(testException), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `complete flow with invalid export settings serialization works correctly`() = runTest {

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // call with invalid JSON
            viewModel.retrieveExportSettings("invalid json")

            // Should transition to FailedToLoadFile
            assertTrue(awaitItem() is ExportResultScreenState.FailedToLoadFile)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- error handling tests -------------------------------------------------------------------------------------------------

    @Test
    fun `retrieveExportSettings handles null export settings serialized`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // call
            viewModel.retrieveExportSettings("null")

            assertTrue(awaitItem() is ExportResultScreenState.FailedToLoadFile)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retrieveExportSettings handles malformed JSON`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // call
            viewModel.retrieveExportSettings("{invalid json}")

            assertTrue(awaitItem() is ExportResultScreenState.FailedToLoadFile)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retrieveExportSettings handles empty string`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // call
            viewModel.retrieveExportSettings("")

            assertTrue(awaitItem() is ExportResultScreenState.FailedToLoadFile)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- edge cases and state transitions ------------------------------------------------------------------------------------



    @Test
    fun `screen state transitions correctly from ReadyToSaveFile to SavingFile`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // Get to ReadyToSaveFile
            viewModel.retrieveExportSettings(exportSettingsSerialized)
            assertEquals(ExportResultScreenState.ReadyToSaveFile(testCsvFile), awaitItem())

            // Transition to SavingFile
            eventBusFlow.emit(SaveFileEvent.SaveFile(testCsvFile))
            assertEquals(ExportResultScreenState.SavingFile, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `screen state transitions correctly from FailedToLoadFile to Loading`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.failure(testException)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // Get to FailedToLoadFile
            viewModel.retrieveExportSettings(exportSettingsSerialized)
            assertEquals(ExportResultScreenState.FailedToLoadFile(testException), awaitItem())

            // Transition to Loading
            eventBusFlow.emit(SaveFileEvent.Idle)
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `screen state transitions correctly through complete save flow`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // Get to ReadyToSaveFile
            viewModel.retrieveExportSettings(exportSettingsSerialized)
            assertEquals(ExportResultScreenState.ReadyToSaveFile(testCsvFile), awaitItem())

            // Start saving
            eventBusFlow.emit(SaveFileEvent.SaveFile(testCsvFile))
            assertEquals(ExportResultScreenState.SavingFile, awaitItem())

            // Save successful
            eventBusFlow.emit(SaveFileEvent.FileSavedSuccessfully)
            assertEquals(ExportResultScreenState.FileSavedSuccessfully, awaitItem())

            // Back to idle
            eventBusFlow.emit(SaveFileEvent.Idle)
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `screen state transitions correctly through save error flow`() = runTest {

        // stub
        coEvery { exportRepository.requestDownloadToDevice(any()) } returns Result.success(testCsvFile)

        viewModel.screenState.test {

            // Initial Loading state
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            // Get to ReadyToSaveFile
            viewModel.retrieveExportSettings(exportSettingsSerialized)
            assertEquals(ExportResultScreenState.ReadyToSaveFile(testCsvFile), awaitItem())

            // Start saving
            eventBusFlow.emit(SaveFileEvent.SaveFile(testCsvFile))
            assertEquals(ExportResultScreenState.SavingFile, awaitItem())

            // Save error
            eventBusFlow.emit(SaveFileEvent.SaveFileError(testException))
            assertEquals(ExportResultScreenState.SaveFileError(testException), awaitItem())

            // Back to idle
            eventBusFlow.emit(SaveFileEvent.Idle)
            assertEquals(ExportResultScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChanged prevents duplicate state emissions`() = runTest {

        viewModel.screenState.test {

            skipItems(1)

            // Emit same event multiple times
            eventBusFlow.emit(SaveFileEvent.Idle)
            eventBusFlow.emit(SaveFileEvent.Idle)
            eventBusFlow.emit(SaveFileEvent.Idle)

            // Should not emit any new states due to distinctUntilChanged
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}