package eu.project.saved.exportWords.vm

import app.cash.turbine.test
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.common.testHelpers.SavedWordTestInstances
import eu.project.saved.exportWords.intent.ExportWordsIntent
import eu.project.saved.exportWords.model.ExportMethod
import eu.project.saved.exportWords.model.ExportMethodVariants
import eu.project.saved.exportWords.model.convertToExportable
import eu.project.saved.exportWords.state.ExportWordsButtonVariants
import eu.project.saved.exportWords.state.ExportWordsScreenState
import eu.project.saved.exportWords.state.ExportWordsSubscreen
import eu.project.saved.exportWords.state.SubscreenControllerButtonVariants
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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

internal object ExportableSavedWordTestInstances {

    private fun SavedWord.exportable(toExport: Boolean) =
        convertToExportable().copy(toExport = toExport)

    val firstFalse = SavedWordTestInstances.first.exportable(false)
    val firstTrue  = SavedWordTestInstances.first.exportable(true)

    val secondFalse = SavedWordTestInstances.second.exportable(false)
    val secondTrue  = SavedWordTestInstances.second.exportable(true)

    val thirdFalse = SavedWordTestInstances.third.exportable(false)
    val thirdTrue  = SavedWordTestInstances.third.exportable(true)
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



//- external data flow tests ------------------------------------------------------------------------------------------

    @Test
    fun `repository data loaded with connectivity shows Loaded screen state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.Loaded, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository data loaded without connectivity shows Disconnected screen state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Disconnected }

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.Disconnected, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository error shows Error screen state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.FailedToLoad("") }

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.Error, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository loading shows Error screen state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loading }

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.Error, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `connectivity changes update screen state correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.screenState.test {

            skipItems(1)

            // start with connected
            connectivityFlow.update { ConnectivityStatus.Connected }
            assertEquals(ExportWordsScreenState.Loaded, awaitItem())

            // switch to disconnected
            connectivityFlow.update { ConnectivityStatus.Disconnected }
            assertEquals(ExportWordsScreenState.Disconnected, awaitItem())

            // switch back to connected
            connectivityFlow.update { ConnectivityStatus.Connected }
            assertEquals(ExportWordsScreenState.Loaded, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository data changes update ui state correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.uiState.test {

            // verify initial data is loaded
            val initialState = awaitItem()
            assertEquals(3, initialState.wordsToExport.size)
            assertTrue(initialState.wordsToExport.any { it.uuid == SavedWordTestInstances.first.uuid })

            // update repository data
            val newData = listOf(SavedWordTestInstances.first, SavedWordTestInstances.second)
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(newData) }

            val updatedState = awaitItem()
            assertEquals(2, updatedState.wordsToExport.size)
            assertTrue(updatedState.wordsToExport.any { it.uuid == SavedWordTestInstances.first.uuid })
            assertTrue(updatedState.wordsToExport.any { it.uuid == SavedWordTestInstances.second.uuid })
            assertFalse(updatedState.wordsToExport.any { it.uuid == SavedWordTestInstances.third.uuid })

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel initializes with correct default states`() = runTest {

        viewModel.uiState.test {

            val initialState = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, initialState.currentSubscreen)
            assertEquals(ExportMethod.NotSpecified, initialState.exportMethod)
            assertTrue(initialState.wordsToExport.isEmpty())
            assertFalse(initialState.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `external flows are observed on initialization`() = runTest {

        // setup - change flows before creating viewModel
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        // create new viewModel instance
        val newViewModel = ExportWordsViewModel(savedWordsRepository, connectivityObserver)

        newViewModel.screenState.test {

            val state = awaitItem()
            assertEquals(ExportWordsScreenState.Loaded, state)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- changeWordSelection tests ------------------------------------------------------------------------------------------

    @Test
    fun `changeWordSelection selects a word correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // call ChangeWordSelection to select first word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            
            assertTrue(firstWord?.toExport == true)
            assertFalse(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection deselects a word correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.uiState.test {

            skipItems(1)

            // first select the word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // then deselect the word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            
            assertFalse(firstWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection selects multiple words correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.uiState.test {

            skipItems(1)

            // select first word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))
            awaitItem()

            // select second word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.secondFalse))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            val secondWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.second.uuid }
            val thirdWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.third.uuid }
            
            assertTrue(firstWord?.toExport == true)
            assertTrue(secondWord?.toExport == true)
            assertFalse(thirdWord?.toExport == true)
            assertFalse(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection deselects all words correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.uiState.test {

            skipItems(1)

            // select all words first
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.secondFalse))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.thirdFalse))
            awaitItem()

            // deselect all words
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.secondFalse))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.thirdFalse))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            val secondWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.second.uuid }
            val thirdWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.third.uuid }
            
            assertFalse(firstWord?.toExport == true)
            assertFalse(secondWord?.toExport == true)
            assertFalse(thirdWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection hides banner when word is selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }
        connectivityFlow.update { ConnectivityStatus.Connected }

        viewModel.uiState.test {

            skipItems(1)

            // first deselect all words to show banner
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstTrue))
            awaitItem()

            // then select a word to hide banner
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))

            val state = awaitItem()
            assertFalse(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection maintains banner state when selecting additional words`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select first word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.firstFalse))
            awaitItem()

            // select second word (banner should remain hidden)
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(ExportableSavedWordTestInstances.secondFalse))

            val state = awaitItem()
            assertFalse(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection called multiple times on same word toggles correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // first call - select
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // second call - deselect
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // third call - select again
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            
            assertTrue(firstWord?.toExport == true)
            assertFalse(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeWordSelection only affects the specified word`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select first word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // select second word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.second.convertToExportable()))
            awaitItem()

            // deselect first word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            val secondWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.second.uuid }
            val thirdWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.third.uuid }
            
            assertFalse(firstWord?.toExport == true)
            assertTrue(secondWord?.toExport == true)
            assertFalse(thirdWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- tryToSwitchToExportSettings tests ------------------------------------------------------------------------------------------

    @Test
    fun `tryToSwitchToExportSettings switches to export settings when words are selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select a word first
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // call TryToSwitchToExportSettings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.ExportSettings, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsEnabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsDisabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings shows banner when no words are selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // call TryToSwitchToExportSettings without selecting any words
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, state.currentSubscreen)
            assertTrue(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings shows banner only once when called multiple times without selection`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // first call - should show banner
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            assertTrue(awaitItem().selectWordsUiState.showNoWordsSelectedBanner)

            // second call - banner should remain shown, no duplicate
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings switches correctly when multiple words are selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select multiple words
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.second.convertToExportable()))
            awaitItem()

            // call TryToSwitchToExportSettings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.ExportSettings, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsEnabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsDisabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings switches correctly when all words are selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select all words
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.second.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.third.convertToExportable()))
            awaitItem()

            // call TryToSwitchToExportSettings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.ExportSettings, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsEnabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsDisabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings called multiple times with words selected maintains stable state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select a word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // first call - should switch
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // second call - should maintain same state
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings switches correctly after deselecting all words`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select a word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // switch to export settings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // switch back to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)
            awaitItem()

            // deselect the word
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // try to switch to export settings again
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, state.currentSubscreen)
            assertTrue(state.selectWordsUiState.showNoWordsSelectedBanner)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToSwitchToExportSettings switches correctly after selecting words after banner was shown`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // try to switch without selection (shows banner)
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // select a word (hides banner)
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()

            // try to switch again (should succeed)
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.ExportSettings, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsEnabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsDisabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- switchToSelectWords tests ------------------------------------------------------------------------------------------

    @Test
    fun `switchToSelectWords switches correctly from export settings subscreen`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // first switch to export settings
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            assertEquals(ExportWordsSubscreen.ExportSettings, awaitItem().currentSubscreen)

            // then switch back to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsDisabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsEnabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switchToSelectWords preserves word selection state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select some words
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.second.convertToExportable()))
            awaitItem()

            // switch to export settings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // switch back to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)

            val state = awaitItem()
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            val secondWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.second.uuid }
            val thirdWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.third.uuid }
            
            assertTrue(firstWord?.toExport == true)
            assertTrue(secondWord?.toExport == true)
            assertFalse(thirdWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switchToSelectWords works correctly when all words are selected`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select all words
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.second.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.third.convertToExportable()))
            awaitItem()

            // switch to export settings
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // switch back to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsDisabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsEnabled, state.subscreenControllerState.exportSettingsButtonState)

            // verify all words are still selected
            val firstWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            val secondWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.second.uuid }
            val thirdWord = state.wordsToExport.find { it.uuid == SavedWordTestInstances.third.uuid }
            
            assertTrue(firstWord?.toExport == true)
            assertTrue(secondWord?.toExport == true)
            assertTrue(thirdWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switchToSelectWords works correctly after export method selection`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select a word and switch to export settings
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // select an export method
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            awaitItem()

            // switch back to select words
            viewModel.onIntent(ExportWordsIntent.SwitchToSelectWords)

            val state = awaitItem()
            assertEquals(ExportWordsSubscreen.SelectWords, state.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsDisabled, state.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsEnabled, state.subscreenControllerState.exportSettingsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- selectExportMethodSend & selectExportMethodDownload tests ----------------------------------------------------------
    @Test
    fun `selectExportMethodSend updates uiState correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // call SelectExportMethodSend
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)

            val state = awaitItem()
            assertEquals(ExportMethod.SendToEmail, state.exportMethod)

            assertEquals(ExportMethodVariants.sendSelected, state.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadNotSelected, state.exportSettingsUiState.downloadMethodState)

            assertFalse(state.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertTrue(state.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.disabled, state.exportSettingsUiState.exportWordsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectExportMethodSend called multiple times maintains stable state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {
            skipItems(1)

            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            awaitItem()

            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectExportMethodDownload updates uiState correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // call SelectExportMethodDownload
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)

            val state = awaitItem()
            assertEquals(ExportMethod.DownloadToDevice, state.exportMethod)

            assertEquals(ExportMethodVariants.sendNotSelected, state.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadSelected, state.exportSettingsUiState.downloadMethodState)

            assertFalse(state.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertFalse(state.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.enabled, state.exportSettingsUiState.exportWordsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectExportMethodDownload called multiple times maintains stable state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)
            awaitItem()

            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switching from Send to Download updates uiState correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // first select Send method
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            val sendState = awaitItem()
            assertEquals(ExportMethod.SendToEmail, sendState.exportMethod)
            assertEquals(ExportMethodVariants.sendSelected, sendState.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadNotSelected, sendState.exportSettingsUiState.downloadMethodState)
            assertFalse(sendState.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertTrue(sendState.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.disabled, sendState.exportSettingsUiState.exportWordsButtonState)

            // then switch to Download method
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)
            val downloadState = awaitItem()
            assertEquals(ExportMethod.DownloadToDevice, downloadState.exportMethod)
            assertEquals(ExportMethodVariants.sendNotSelected, downloadState.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadSelected, downloadState.exportSettingsUiState.downloadMethodState)
            assertFalse(downloadState.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertTrue(sendState.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.enabled, downloadState.exportSettingsUiState.exportWordsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switching from Download to Send updates uiState correctly`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // first select Download method
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)
            val downloadState = awaitItem()
            assertEquals(ExportMethod.DownloadToDevice, downloadState.exportMethod)
            assertEquals(ExportMethodVariants.sendNotSelected, downloadState.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadSelected, downloadState.exportSettingsUiState.downloadMethodState)
            assertFalse(downloadState.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertFalse(downloadState.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.enabled, downloadState.exportSettingsUiState.exportWordsButtonState)

            // then switch to Send method
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            val sendState = awaitItem()
            assertEquals(ExportMethod.SendToEmail, sendState.exportMethod)
            assertEquals(ExportMethodVariants.sendSelected, sendState.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadNotSelected, sendState.exportSettingsUiState.downloadMethodState)
            assertFalse(sendState.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertTrue(sendState.exportSettingsUiState.showExportMethodNotAvailableBanner)
            assertEquals(ExportWordsButtonVariants.disabled, sendState.exportSettingsUiState.exportWordsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `rapid switching between export methods maintains correct state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // rapid switching sequence: Send -> Download -> Send -> Download
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            awaitItem()
            
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)
            awaitItem()
            
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            awaitItem()
            
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)

            val finalState = awaitItem()
            assertEquals(ExportMethod.DownloadToDevice, finalState.exportMethod)
            assertEquals(ExportMethodVariants.sendNotSelected, finalState.exportSettingsUiState.sendMethodState)
            assertEquals(ExportMethodVariants.downloadSelected, finalState.exportSettingsUiState.downloadMethodState)
            assertFalse(finalState.exportSettingsUiState.emailTextFieldUiState.isVisible)
            assertEquals(ExportWordsButtonVariants.enabled, finalState.exportSettingsUiState.exportWordsButtonState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switching export methods preserves other ui state`() = runTest {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(SavedWordTestInstances.list) }

        viewModel.uiState.test {

            skipItems(1)

            // select some words and switch to export settings
            viewModel.onIntent(ExportWordsIntent.ChangeWordSelection(SavedWordTestInstances.first.convertToExportable()))
            awaitItem()
            viewModel.onIntent(ExportWordsIntent.TryToSwitchToExportSettings)
            awaitItem()

            // switch between export methods
            viewModel.onIntent(ExportWordsIntent.SelectExportMethodSend)
            skipItems(1)

            viewModel.onIntent(ExportWordsIntent.SelectExportMethodDownload)
            val downloadState = awaitItem()

            // verify export method changed
            assertEquals(ExportMethod.DownloadToDevice, downloadState.exportMethod)
            assertFalse(downloadState.exportSettingsUiState.emailTextFieldUiState.isVisible)

            // verify other state is preserved
            assertEquals(ExportWordsSubscreen.ExportSettings, downloadState.currentSubscreen)
            assertEquals(SubscreenControllerButtonVariants.selectWordsEnabled, downloadState.subscreenControllerState.selectWordsButtonState)
            assertEquals(SubscreenControllerButtonVariants.exportSettingsDisabled, downloadState.subscreenControllerState.exportSettingsButtonState)
            
            val firstWord = downloadState.wordsToExport.find { it.uuid == SavedWordTestInstances.first.uuid }
            assertTrue(firstWord?.toExport == true)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}



//----------------------------------------------------------------------------------------------------------------------