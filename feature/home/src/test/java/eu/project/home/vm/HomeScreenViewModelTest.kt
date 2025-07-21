package eu.project.home.vm

import app.cash.turbine.test
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.connectivity.ConnectivityStatus
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HomeScreenViewModelTest {

    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setUp() {
        connectivityObserver = mockk<ConnectivityObserver>()
    }

    @Test
    fun `isNetworkAvailable emits true when status is Connected, false when Disconnected`() = runTest(StandardTestDispatcher()) {

        val statusFlow = MutableSharedFlow<ConnectivityStatus>()
        every { connectivityObserver.connectivityStatus } returns statusFlow

        viewModel = HomeScreenViewModel(connectivityObserver)

        viewModel.isNetworkAvailable.test {

            // false by default
            assertEquals(false, awaitItem())

            // true when connected
            statusFlow.emit(ConnectivityStatus.Connected)
            assertEquals(true, awaitItem())

            // false when disconnected
            statusFlow.emit(ConnectivityStatus.Disconnected)
            assertEquals(false, awaitItem())
        }
    }
}