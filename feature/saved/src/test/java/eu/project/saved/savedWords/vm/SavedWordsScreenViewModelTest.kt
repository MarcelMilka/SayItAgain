package eu.project.saved.savedWords.vm

import app.cash.turbine.test
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.model.SavedWordsScreenViewState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID

class SavedWordsScreenViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    val ioCoroutineScope = CoroutineScope(testDispatcher)

    private lateinit var savedWordsRepository: SavedWordsRepository
    private lateinit var savedWordsScreenViewModel: SavedWordsScreenViewModel

    private lateinit var dataStateFlow: MutableStateFlow<SavedWordsRepositoryDataState>

    private val firstEntityInstance = SavedWord(
        uuid = UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"),
        word = "Cat",
        language = "English"
    )
    private val secondEntityInstance = SavedWord(
        uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        word = "Monitor lizard",
        language = "English"
    )
    private val thirdEntityInstance = SavedWord(
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        word = "Hagetreboa",
        language = "Norwegian"
    )

    @Before
    fun setUp() {

        savedWordsRepository = mockk()
        dataStateFlow = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)
    }

    @After
    fun tearDown() {

        testDispatcher.cancel()
        ioCoroutineScope.cancel()
    }



    @Test
    fun `uiState emits Loading when repository state is Loading`() = runTest(testDispatcher) {

        // stub
        every { savedWordsRepository.dataState } returns dataStateFlow

        // run
        savedWordsScreenViewModel = SavedWordsScreenViewModel(savedWordsRepository)

        // test
        savedWordsScreenViewModel.uiState.test {

            assertEquals(SavedWordsScreenViewState.Loading, this.awaitItem())
        }
    }

    @Test
    fun `uiState emits NoData when repository emits Loaded_NoData`() = runTest(testDispatcher) {

        // stub
        every { savedWordsRepository.dataState } returns dataStateFlow

        // run
        savedWordsScreenViewModel = SavedWordsScreenViewModel(savedWordsRepository)

        // test
        savedWordsScreenViewModel.uiState.test {

            // Loading by default
            assertEquals(SavedWordsScreenViewState.Loading, this.awaitItem())

            // NoData
            dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.NoData
            assertEquals(SavedWordsScreenViewState.Loaded.NoData, this.awaitItem())
        }
    }

    @Test
    fun `uiState emits NoData when repository emits Loaded_Data`() = runTest(testDispatcher) {

        // stub
        every { savedWordsRepository.dataState } returns dataStateFlow

        // run
        savedWordsScreenViewModel = SavedWordsScreenViewModel(savedWordsRepository)

        // test
        savedWordsScreenViewModel.uiState.test {

            // Loading by default
            assertEquals(SavedWordsScreenViewState.Loading, this.awaitItem())

            // Data
            val retrievedData = listOf(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
            dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
            assertEquals(SavedWordsScreenViewState.Loaded.Data(retrievedData), this.awaitItem())
        }
    }

    @Test
    fun `uiState emits FailedToLoad when repository emits FailedToLoad`() = runTest(testDispatcher) {

        // stub
        every { savedWordsRepository.dataState } returns dataStateFlow

        // run
        savedWordsScreenViewModel = SavedWordsScreenViewModel(savedWordsRepository)

        // test
        savedWordsScreenViewModel.uiState.test {

            // Loading by default
            assertEquals(SavedWordsScreenViewState.Loading, this.awaitItem())

            // FailedToLoad
            dataStateFlow.value = SavedWordsRepositoryDataState.FailedToLoad("Exemplary cause")
            assertEquals(SavedWordsScreenViewState.FailedToLoad("Exemplary cause"), this.awaitItem())
        }
    }



    @Test
    fun `deleteWord calls repository deleteWord`() = runTest(testDispatcher) {

        // stub
        val retrievedData = listOf(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(retrievedData)

        every { savedWordsRepository.dataState } returns dataStateFlow
        coEvery { savedWordsRepository.deleteWord(any()) } returns Unit

        // run
        savedWordsScreenViewModel = SavedWordsScreenViewModel(savedWordsRepository)

        // test
        savedWordsScreenViewModel.deleteWord(firstEntityInstance)

        // verify
        coVerify(exactly = 1) { savedWordsRepository.deleteWord(firstEntityInstance) }
    }
}