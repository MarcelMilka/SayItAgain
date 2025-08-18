package eu.project.home.vm

import app.cash.turbine.test
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // dependencies
    private val connectivityObserver = mockk<ConnectivityObserver>(relaxed = true)
    private lateinit var connectivityFlow: MutableStateFlow<ConnectivityStatus>

    // tested class
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {

        connectivityFlow = MutableStateFlow(ConnectivityStatus.Connected)

        every { connectivityObserver.connectivityStatus } returns connectivityFlow

        viewModel = HomeViewModel(connectivityObserver)
    }

    //- connectivity status tests ------------------------------------------------------------------------------------------

    @Test
    fun `isNetworkAvailable emits true when connectivity status is Connected`() = runTest {

        // setup
        connectivityFlow.value = ConnectivityStatus.Connected

        // test
        viewModel.isNetworkAvailable.test {
            assertTrue(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isNetworkAvailable emits false when connectivity status is Disconnected`() = runTest {

        // setup
        connectivityFlow.value = ConnectivityStatus.Disconnected

        // test
        viewModel.isNetworkAvailable.test {

            assertFalse(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isNetworkAvailable emits initial value of true`() = runTest {

        viewModel.isNetworkAvailable.test {

            assertTrue(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `connectivity status changes update isNetworkAvailable correctly`() = runTest {

        viewModel.isNetworkAvailable.test {

            // initial value should be true
            assertTrue(awaitItem())

            // change to disconnected
            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            // change back to connected
            connectivityFlow.value = ConnectivityStatus.Connected
            assertTrue(awaitItem())

            // change to disconnected again
            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `rapid connectivity status changes are handled correctly`() = runTest {

        viewModel.isNetworkAvailable.test {

            // initial value
            assertTrue(awaitItem())

            // rapid changes
            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            connectivityFlow.value = ConnectivityStatus.Connected
            assertTrue(awaitItem())

            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            connectivityFlow.value = ConnectivityStatus.Connected
            assertTrue(awaitItem())

            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel initializes with correct default state`() = runTest {

        viewModel.isNetworkAvailable.test {

            val initialState = awaitItem()
            assertTrue(initialState)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `external connectivity flow is observed on initialization`() = runTest {

        // setup
        connectivityFlow.value = ConnectivityStatus.Disconnected

        // create new viewModel instance
        val newViewModel = HomeViewModel(connectivityObserver)

        // test
        newViewModel.isNetworkAvailable.test {

            val state = awaitItem()
            assertFalse(state)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `connectivity observer is properly injected and observed`() = runTest {

        viewModel.isNetworkAvailable.test {

            // initial state
            assertTrue(awaitItem())

            // verify changes are reflected
            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            connectivityFlow.value = ConnectivityStatus.Connected
            assertTrue(awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChanged prevents duplicate emissions`() = runTest {

        viewModel.isNetworkAvailable.test {

            // initial value
            assertTrue(awaitItem())

            // emit same status multiple times
            connectivityFlow.value = ConnectivityStatus.Connected
            connectivityFlow.value = ConnectivityStatus.Connected
            connectivityFlow.value = ConnectivityStatus.Connected

            // should not emit duplicate values
            expectNoEvents()

            // change to different status
            connectivityFlow.value = ConnectivityStatus.Disconnected
            assertFalse(awaitItem())

            // emit same status again
            connectivityFlow.value = ConnectivityStatus.Disconnected
            connectivityFlow.value = ConnectivityStatus.Disconnected

            // should not emit duplicate values
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }
}