package eu.project.saved.exportWords.vm

import app.cash.turbine.test
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.exportWords.model.ExportWordsScreenState
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val dataStateFlow = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)

    private val connectivityObserver = mockk<ConnectivityObserver>(relaxed = true)
    private val connectivityFlow = MutableStateFlow(ConnectivityStatus.Disconnected)

    // data
    private val firstInstance = SavedWord(
        UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English"
    )
    private val secondInstance = SavedWord(
        UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English"
    )
    private val thirdInstance = SavedWord(
        UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian"
    )
    private val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)

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

        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
        connectivityFlow.value = ConnectivityStatus.Connected

        viewModel.screenState.test {

            assertEquals(ExportWordsScreenState.ReadyToExport, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Disconnected when DataLoaded and Disconnected`() = runTest {

        dataStateFlow.value = SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
        connectivityFlow.value = ConnectivityStatus.Disconnected

        viewModel.screenState.test {
            assertEquals(ExportWordsScreenState.Disconnected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}