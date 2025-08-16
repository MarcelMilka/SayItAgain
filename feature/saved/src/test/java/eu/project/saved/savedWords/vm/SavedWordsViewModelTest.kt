package eu.project.saved.savedWords.vm

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.testHelpers.SavedWordTestInstances
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SavedWordsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // dependencies
    private val savedWordsRepository = mockk<SavedWordsRepository>(relaxed = true)
    private var dataStateFlow = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)

    // tested class
    private lateinit var viewModel: SavedWordsViewModel

    @Before
    fun setup() {

        every { savedWordsRepository.dataState } returns dataStateFlow
        viewModel = SavedWordsViewModel(savedWordsRepository)
    }



//- external data flow tests ------------------------------------------------------------------------------------------

    @Test
    fun `repository loading state shows Loading screen state`() = runTest(testDispatcher) {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loading }

        // test
        viewModel.screenState.test {

            assertEquals(SavedWordsScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository no data state shows NoData screen state`() = runTest(testDispatcher) {

        // setup
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.NoData }

        // test
        viewModel.screenState.test {

            assertEquals(SavedWordsScreenState.Loaded.NoData, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository data state shows Data screen state with correct data`() = runTest(testDispatcher) {

        // setup
        val testData = SavedWordTestInstances.list
        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(testData) }

        // test
        viewModel.screenState.test {

            val state = awaitItem()
            assertEquals(SavedWordsScreenState.Loaded.Data(testData), state)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository error state shows Error screen state with correct cause`() = runTest(testDispatcher) {

        // setup
        val errorCause = "Test error message"
        dataStateFlow.update { SavedWordsRepositoryDataState.FailedToLoad(errorCause) }

        // test
        viewModel.screenState.test {

            val state = awaitItem()
            assertEquals(SavedWordsScreenState.Error(errorCause), state)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `repository state changes update screen state correctly`() = runTest(testDispatcher) {

        viewModel.screenState.test {

            // initial loading state
            assertEquals(SavedWordsScreenState.Loading, awaitItem())

            // change to no data
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.NoData }
            assertEquals(SavedWordsScreenState.Loaded.NoData, awaitItem())

            // change to data
            val testData = SavedWordTestInstances.list
            dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(testData) }
            assertEquals(SavedWordsScreenState.Loaded.Data(testData), awaitItem())

            // change to error
            dataStateFlow.update { SavedWordsRepositoryDataState.FailedToLoad("Error") }
            assertEquals(SavedWordsScreenState.Error("Error"), awaitItem())

            // change back to loading
            dataStateFlow.update { SavedWordsRepositoryDataState.Loading }
            assertEquals(SavedWordsScreenState.Loading, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel initializes with correct default states`() = runTest(testDispatcher) {

        // test
        viewModel.dialogState.test {

            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- requestWordDeletion tests ------------------------------------------------------------------------------------------

    @Test
    fun `requestWordDeletion shows dialog with correct word`() = runTest(testDispatcher) {

        val wordToDelete = SavedWordTestInstances.first

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `requestWordDeletion called multiple times shows dialog with latest word`() = runTest(testDispatcher) {

        val firstWord = SavedWordTestInstances.first
        val secondWord = SavedWordTestInstances.second

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request deletion of first word
            viewModel.requestWordDeletion(firstWord)
            assertEquals(DiscardWordDialogState.Visible(firstWord), awaitItem())

            // request deletion of second word
            viewModel.requestWordDeletion(secondWord)
            assertEquals(DiscardWordDialogState.Visible(secondWord), awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `requestWordDeletion works correctly with different words`() = runTest(testDispatcher) {

        val words = listOf(
            SavedWordTestInstances.first,
            SavedWordTestInstances.second,
            SavedWordTestInstances.third
        )

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // test each word
            words.forEach { word ->
                viewModel.requestWordDeletion(word)
                assertEquals(DiscardWordDialogState.Visible(word), awaitItem())
            }

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- cancelWordDeletion tests ------------------------------------------------------------------------------------------

    @Test
    fun `cancelWordDeletion hides dialog when visible`() = runTest(testDispatcher) {

        val wordToDelete = SavedWordTestInstances.first

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // cancel word deletion
            viewModel.cancelWordDeletion()
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cancelWordDeletion called when dialog already hidden maintains hidden state`() = runTest(testDispatcher) {

        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // cancel word deletion without showing dialog first
            viewModel.cancelWordDeletion()

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cancelWordDeletion called multiple times maintains hidden state`() = runTest(testDispatcher) {

        val wordToDelete = SavedWordTestInstances.first

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // cancel word deletion multiple times
            viewModel.cancelWordDeletion()
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            viewModel.cancelWordDeletion()
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cancelWordDeletion works correctly after multiple requestWordDeletion calls`() = runTest(testDispatcher) {

        val firstWord = SavedWordTestInstances.first
        val secondWord = SavedWordTestInstances.second

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request deletion of first word
            viewModel.requestWordDeletion(firstWord)
            assertEquals(DiscardWordDialogState.Visible(firstWord), awaitItem())

            // request deletion of second word
            viewModel.requestWordDeletion(secondWord)
            assertEquals(DiscardWordDialogState.Visible(secondWord), awaitItem())

            // cancel word deletion
            viewModel.cancelWordDeletion()
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- deleteWord tests ------------------------------------------------------------------------------------------

    @Test
    fun `deleteWord calls repository deleteWord and hides dialog`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // delete the word
            viewModel.deleteWord(wordToDelete)
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }

    @Test
    fun `deleteWord calls repository with correct word parameter`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.second
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        viewModel.deleteWord(wordToDelete)

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }

    @Test
    fun `deleteWord called multiple times calls repository for each word`() = runTest(testDispatcher) {

        // setup
        val words = listOf(
            SavedWordTestInstances.first,
            SavedWordTestInstances.second,
            SavedWordTestInstances.third
        )
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // delete multiple words
        words.forEach { word ->

            viewModel.deleteWord(word)
        }

        // verify each word was deleted
        words.forEach { word ->

            coVerify(exactly = 1) { savedWordsRepository.deleteWord(word) }
        }
    }

    @Test
    fun `deleteWord hides dialog even when called without showing dialog first`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // delete word without showing dialog first
            viewModel.deleteWord(wordToDelete)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }

    @Test
    fun `deleteWord called after cancelWordDeletion works correctly`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // cancel word deletion
            viewModel.cancelWordDeletion()
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // delete the word
            viewModel.deleteWord(wordToDelete)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }

    @Test
    fun `deleteWord called multiple times on same word calls repository multiple times`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // delete same word multiple times
        viewModel.deleteWord(wordToDelete)
        viewModel.deleteWord(wordToDelete)
        viewModel.deleteWord(wordToDelete)

        // verify
        coVerify(exactly = 3) { savedWordsRepository.deleteWord(wordToDelete) }
    }



//- integration tests ------------------------------------------------------------------------------------------

    @Test
    fun `complete word deletion flow works correctly`() = runTest(testDispatcher) {

        // setup
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // 1. Request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // 2. Cancel word deletion
            viewModel.cancelWordDeletion()
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // 3. Request word deletion again
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), awaitItem())

            // 4. Delete the word
            viewModel.deleteWord(wordToDelete)
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }

    @Test
    fun `multiple word deletion flows work correctly`() = runTest(testDispatcher) {

        // setup
        val firstWord = SavedWordTestInstances.first
        val secondWord = SavedWordTestInstances.second
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // test
        viewModel.dialogState.test {

            // hidden by default
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // Delete first word
            viewModel.requestWordDeletion(firstWord)
            assertEquals(DiscardWordDialogState.Visible(firstWord), awaitItem())
            viewModel.deleteWord(firstWord)
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            // Delete second word
            viewModel.requestWordDeletion(secondWord)
            assertEquals(DiscardWordDialogState.Visible(secondWord), awaitItem())
            viewModel.deleteWord(secondWord)
            assertEquals(DiscardWordDialogState.Hidden, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(firstWord) }
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(secondWord) }
    }

    @Test
    fun `screen state and dialog state work independently`() = runTest(testDispatcher) {

        // setup
        val testData = SavedWordTestInstances.list
        val wordToDelete = SavedWordTestInstances.first
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        dataStateFlow.update { SavedWordsRepositoryDataState.Loaded.Data(testData) }

        turbineScope {

            val screenStateFlow = viewModel.screenState.testIn(backgroundScope)
            val dialogStateFlow = viewModel.dialogState.testIn(backgroundScope)

            // DiscardWordDialogState is Hidden by default
            assertEquals(SavedWordsScreenState.Loaded.Data(testData), screenStateFlow.awaitItem())
            assertEquals(DiscardWordDialogState.Hidden, dialogStateFlow.awaitItem())
            screenStateFlow.expectNoEvents()
            screenStateFlow.cancelAndIgnoreRemainingEvents()

            // request word deletion
            viewModel.requestWordDeletion(wordToDelete)
            assertEquals(DiscardWordDialogState.Visible(wordToDelete), dialogStateFlow.awaitItem())

            // delete the word
            viewModel.deleteWord(wordToDelete)
            assertEquals(DiscardWordDialogState.Hidden, dialogStateFlow.awaitItem())

            dialogStateFlow.expectNoEvents()
            dialogStateFlow.cancelAndIgnoreRemainingEvents()
        }

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(wordToDelete) }
    }
}