package eu.project.saved.exportWords.vm

import app.cash.turbine.test
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.exportWords.intent.ExportWordsIntent
import eu.project.saved.exportWords.model.ExportWordsScreenState
import eu.project.saved.exportWords.model.ExportWordsSubscreen
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.model.convertToExportable
import eu.project.saved.exportWords.ui.SubscreenControllerButtonVariants
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.UUID

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class ExportWordsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // dependencies
    private val savedWordsRepository = mockk<SavedWordsRepository>(relaxed = true)
    private var dataStateFlow = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)

    private val connectivityObserver = mockk<ConnectivityObserver>(relaxed = true)
    private var connectivityFlow = MutableStateFlow(ConnectivityStatus.Disconnected)

    // data
    private val firstInstance = SavedWord(UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English")
    private val firstExportableInstance = ExportableSavedWord(UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English", false)

    private val secondInstance = SavedWord(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English")
    private val secondExportableInstance = ExportableSavedWord(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English", false)

    private val thirdInstance = SavedWord(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian")
    private val thirdExportableInstance = ExportableSavedWord(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian", false)

    private val savedWords = listOf(firstInstance, secondInstance, thirdInstance)
    private val exportableSavedWords = listOf(firstExportableInstance, secondExportableInstance, thirdExportableInstance)
    private val exportableSavedWordsFirstTrue = listOf(firstExportableInstance.copy(toExport = true), secondExportableInstance, thirdExportableInstance)


    private val exportableWords = listOf(firstInstance.convertToExportable(), secondInstance.convertToExportable(), thirdInstance.convertToExportable())

    // tested class
    private lateinit var viewModel: ExportWordsViewModel

    @Before
    fun setup() {

        every { savedWordsRepository.dataState } returns dataStateFlow
        every { connectivityObserver.connectivityStatus } returns connectivityFlow

        viewModel = ExportWordsViewModel(
            savedWordsRepository,
            connectivityObserver
        )
    }



    // screenState tests
    @Test
    fun `emits Error when Loading`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.Loading
        connectivityFlow.value = ConnectivityStatus.Connected

        viewModel.screenState.test {
            assertEquals(ExportWordsScreenState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when NoData`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.NoData
        connectivityFlow.value = ConnectivityStatus.Connected

        viewModel.screenState.test {
            assertEquals(ExportWordsScreenState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when FailedToLoad`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.FailedToLoad("")
        connectivityFlow.value = ConnectivityStatus.Connected

        viewModel.screenState.test {
            assertEquals(ExportWordsScreenState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits ReadyToExport when DataLoaded and Connected`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(savedWords)
        connectivityFlow.value = ConnectivityStatus.Connected

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.ReadyToExport, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Disconnected when DataLoaded and Disconnected`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(savedWords)
        connectivityFlow.value = ConnectivityStatus.Disconnected

        viewModel.screenState.test {
            assertEquals(ExportWordsScreenState.Disconnected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }



    // uiState tests
    @Test
    fun `uiState emits empty exportableWords when ViewModel is initialized`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits exportableWords when dataState changes to Loaded_Data`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }

            assertEquals(exportableWords, awaitItem().exportableWords)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState does not update exportableWords when dataState changes to Loaded_NoData`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.NoData }

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState retains exportableWords when dataState changes from Loaded_Data to FailedToLoad`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.FailedToLoad("error") }

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState does not emit duplicate exportableWords when Loaded_Data is emitted twice with same data`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState does not change when connectivity status changes to Disconnected`() = runTest {

        viewModel.uiState.test {
            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            connectivityFlow.update { ConnectivityStatus.Disconnected }

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits empty exportableWords when Loaded_Data contains empty list`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(emptyList()) }

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }



    // ChangeWordSelection tests
    @Test
    fun `ChangeWordSelection toggles toExport for the first word`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(firstExportableInstance))
            assertEquals(exportableSavedWordsFirstTrue, awaitItem().exportableWords)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChangeWordSelection toggles toExport back to false`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableSavedWords, awaitItem().exportableWords)

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(firstExportableInstance))
            assertEquals(exportableSavedWordsFirstTrue, awaitItem().exportableWords)

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(firstExportableInstance))
            assertEquals(exportableSavedWords, awaitItem().exportableWords)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChangeWordSelection toggles toExport for all words individually`() = runTest {

        viewModel.uiState.test {

            // Step 1: Initial state
            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            // Step 2: Load words
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }

            val loadedWords = awaitItem().exportableWords
            assertEquals(exportableSavedWords, loadedWords)

            // Step 3: Toggle each word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(firstExportableInstance))
            val afterFirstToggle = awaitItem().exportableWords

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(secondExportableInstance))
            val afterSecondToggle = awaitItem().exportableWords

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(thirdExportableInstance))
            val afterThirdToggle = awaitItem().exportableWords

            // Step 4: Expected
            val expected = listOf(
                firstExportableInstance.copy(toExport = true),
                secondExportableInstance.copy(toExport = true),
                thirdExportableInstance.copy(toExport = true)
            )

            assertEquals(expected, afterThirdToggle)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChangeWordSelection does nothing if word not in list`() = runTest {

        viewModel.uiState.test {

            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableSavedWords, awaitItem().exportableWords)

            val notExistingWord = ExportableSavedWord(UUID.randomUUID(), "Ghost", "Unknown")

            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(notExistingWord))

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



    // TryToSwitchToExportSettings tests
    @Test
    fun `TryToSwitchToExportSettings sets correct UI state when at least one word is selected`() = runTest {

        viewModel.uiState.test {

            // default
            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            // set LoadedData
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            // select word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(firstExportableInstance))
            assertEquals(exportableSavedWordsFirstTrue, awaitItem().exportableWords)

            // switch to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToExportSettings)
            val state = awaitItem().subscreenControllerState

            assertEquals(ExportWordsSubscreen.ExportSettings, state.exportWordsSubscreen)
            assertEquals(SubscreenControllerButtonVariants.leftInactive, state.leftButton)
            assertEquals(SubscreenControllerButtonVariants.rightActive, state.rightButton)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `TryToSwitchToExportSettings does not change UI state when any of the words is selected`() = runTest {

        viewModel.uiState.test {

            // default
            assertEquals(emptyList<ExportableSavedWord>(), awaitItem().exportableWords)

            // set LoadedData
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(savedWords) }
            assertEquals(exportableWords, awaitItem().exportableWords)

            // switch to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToExportSettings)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    // SwitchToExportSettings tests
    @Test
    fun `SwitchToSelectWords sets correct UI state`() = runTest {

        viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)

        val state = viewModel.uiState.value.subscreenControllerState
        assertEquals(ExportWordsSubscreen.SelectWords, state.exportWordsSubscreen)
        assertEquals(SubscreenControllerButtonVariants.leftActive, state.leftButton)
        assertEquals(SubscreenControllerButtonVariants.rightInactive, state.rightButton)
    }
}