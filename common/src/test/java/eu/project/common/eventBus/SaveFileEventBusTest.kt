package eu.project.common.eventBus

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import eu.project.common.eventBus.saveFile.SaveFileEvent
import eu.project.common.eventBus.saveFile.SaveFileEventBus
import eu.project.common.remoteData.CsvFile
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
class SaveFileEventBusTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // tested class
    private lateinit var eventBus: SaveFileEventBus

    private val json = """[{"Term":"Lorem","Definition":"Ipsum"},{"Term":"Dolor","Definition":"Sit"}]"""
    private val csvFile = CsvFile(data = json.toByteArray())
    private val testException = RuntimeException("Test error")

    @Before
    fun setup() {
        eventBus = SaveFileEventBus()
    }



// - Event emission tests ----------------------------------------------------------------------------------------------

    @Test
    fun `emit Idle event is collected correctly`() = runTest {

        eventBus.events.test {

            // Emit Idle event
            eventBus.emit(SaveFileEvent.Idle)

            val event = awaitItem()
            assertEquals(SaveFileEvent.Idle, event)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emit SaveFile event is collected correctly`() = runTest {

        eventBus.events.test {

            // Emit SaveFile event
            eventBus.emit(SaveFileEvent.SaveFile(csvFile))

            val event = awaitItem()
            assertEquals(SaveFileEvent.SaveFile(csvFile), event)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emit FileSavedSuccessfully event is collected correctly`() = runTest {

        eventBus.events.test {

            // Emit FileSavedSuccessfully event
            eventBus.emit(SaveFileEvent.FileSavedSuccessfully)

            val event = awaitItem()
            assertEquals(SaveFileEvent.FileSavedSuccessfully, event)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emit SaveFileError event is collected correctly`() = runTest {

        eventBus.events.test {

            // Emit SaveFileError event
            eventBus.emit(SaveFileEvent.SaveFileError(testException))

            val event = awaitItem()
            assertEquals(SaveFileEvent.SaveFileError(testException), event)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- Multiple events emission tests -------------------------------------------------------------------------------------

    @Test
    fun `multiple events are emitted and collected in order`() = runTest {

        eventBus.events.test {

            // Emit multiple events
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.SaveFile(csvFile))
            eventBus.emit(SaveFileEvent.FileSavedSuccessfully)

            // Verify events are collected in order
            assertEquals(SaveFileEvent.Idle, awaitItem())

            assertEquals(SaveFileEvent.SaveFile(csvFile), awaitItem())

            assertEquals(SaveFileEvent.FileSavedSuccessfully, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `rapid event emission works correctly`() = runTest {

        eventBus.events.test {

            // Emit events rapidly
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.SaveFile(csvFile))
            eventBus.emit(SaveFileEvent.SaveFileError(testException))
            eventBus.emit(SaveFileEvent.FileSavedSuccessfully)

            // Verify all events are collected
            assertEquals(SaveFileEvent.Idle, awaitItem())
            assertEquals(SaveFileEvent.SaveFile(csvFile), awaitItem())
            assertEquals(SaveFileEvent.SaveFileError(testException), awaitItem())
            assertEquals(SaveFileEvent.FileSavedSuccessfully, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- Multiple collectors tests ------------------------------------------------------------------------------------------

    @Test
    fun `multiple collectors receive the same events`() = runTest {

        // Create two collectors
        turbineScope {

            val c1 = eventBus.events.testIn(backgroundScope)
            val c2 = eventBus.events.testIn(backgroundScope)

            // Emit an event
            eventBus.emit(SaveFileEvent.SaveFile(csvFile))

            // Both collectors should receive the event
            val event1 = c1.awaitItem()
            val event2 = c2.awaitItem()

            assertEquals(SaveFileEvent.SaveFile(csvFile), event1)
            assertEquals(SaveFileEvent.SaveFile(csvFile), event2)
            assertEquals(event1, event2)

            c1.expectNoEvents()
            c2.expectNoEvents()
            c1.cancelAndIgnoreRemainingEvents()
            c2.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `new collector does not receive replay buffer event`() = runTest {

        // Emit an event first
        eventBus.emit(SaveFileEvent.SaveFile(csvFile))

        // Create a new collector after emission
        eventBus.events.test {

            // New collector should not receive any events since replay = 0
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `replay buffer is disabled`() = runTest {

        // Emit multiple events
        eventBus.emit(SaveFileEvent.Idle)
        eventBus.emit(SaveFileEvent.SaveFile(csvFile))
        eventBus.emit(SaveFileEvent.FileSavedSuccessfully)

        // Create a new collector
        eventBus.events.test {

            // Should not receive any events since replay = 0
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- Event bus lifecycle tests ------------------------------------------------------------------------------------------

    @Test
    fun `event bus starts with no events`() = runTest {

        eventBus.events.test {

            // Should not emit any events initially
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `event bus handles multiple emissions of same event type`() = runTest {

        eventBus.events.test {

            // Emit same event type multiple times
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.Idle)

            // Should receive all three events
            assertEquals(SaveFileEvent.Idle, awaitItem())
            assertEquals(SaveFileEvent.Idle, awaitItem())
            assertEquals(SaveFileEvent.Idle, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `event bus handles mixed event types correctly`() = runTest {

        eventBus.events.test {

            // Emit mixed event types
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.SaveFile(csvFile))
            eventBus.emit(SaveFileEvent.Idle)
            eventBus.emit(SaveFileEvent.SaveFileError(testException))
            eventBus.emit(SaveFileEvent.FileSavedSuccessfully)

            // Verify all events are received in order
            assertEquals(SaveFileEvent.Idle, awaitItem())
            assertEquals(SaveFileEvent.SaveFile(csvFile), awaitItem())
            assertEquals(SaveFileEvent.Idle, awaitItem())
            assertEquals(SaveFileEvent.SaveFileError(testException), awaitItem())
            assertEquals(SaveFileEvent.FileSavedSuccessfully, awaitItem())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- Error handling tests ------------------------------------------------------------------------------------------

    @Test
    fun `event bus handles different types of exceptions`() = runTest {

        eventBus.events.test {

            val runtimeException = RuntimeException("Runtime error")
            val illegalArgumentException = IllegalArgumentException("Illegal argument")
            val nullPointerException = NullPointerException("Null pointer")

            // Emit different exception types
            eventBus.emit(SaveFileEvent.SaveFileError(runtimeException))
            eventBus.emit(SaveFileEvent.SaveFileError(illegalArgumentException))
            eventBus.emit(SaveFileEvent.SaveFileError(nullPointerException))

            // Verify all exceptions are received
            val error1 = awaitItem()
            assertEquals(SaveFileEvent.SaveFileError(runtimeException), error1)

            val error2 = awaitItem()
            assertEquals(SaveFileEvent.SaveFileError(illegalArgumentException), error2)

            val error3 = awaitItem()
            assertEquals(SaveFileEvent.SaveFileError(nullPointerException), error3)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }



//- Concurrent access tests ------------------------------------------------------------------------------------------

    @Test
    fun `event bus handles concurrent emissions`() = runTest {

        eventBus.events.test {

            // Emit events concurrently
            val job1 = launch {
                eventBus.emit(SaveFileEvent.Idle)
            }
            val job2 = launch {
                eventBus.emit(SaveFileEvent.SaveFile(csvFile))
            }
            val job3 = launch {
                eventBus.emit(SaveFileEvent.FileSavedSuccessfully)
            }

            // Wait for all emissions to complete
            job1.join()
            job2.join()
            job3.join()

            // Should receive all three events (order may vary due to concurrency)
            val events = mutableListOf<SaveFileEvent>()
            repeat(3) {
                events.add(awaitItem())
            }

            assertEquals(3, events.size)
            assertTrue(events.any { it is SaveFileEvent.Idle })
            assertTrue(events.any { it is SaveFileEvent.SaveFile })
            assertTrue(events.any { it is SaveFileEvent.FileSavedSuccessfully })

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `event bus maintains event order under stress`() = runTest {

        eventBus.events.test {

            // Emit many events rapidly
            repeat(100) { index ->
                when (index % 4) {
                    0 -> eventBus.emit(SaveFileEvent.Idle)
                    1 -> eventBus.emit(SaveFileEvent.SaveFile(csvFile))
                    2 -> eventBus.emit(SaveFileEvent.FileSavedSuccessfully)
                    3 -> eventBus.emit(SaveFileEvent.SaveFileError(testException))
                }
            }

            // Verify we receive all 100 events
            repeat(100) { index ->
                val event = awaitItem()
                when (index % 4) {
                    0 -> assertEquals(SaveFileEvent.Idle, event)
                    1 -> assertEquals(SaveFileEvent.SaveFile(csvFile), event)
                    2 -> assertEquals(SaveFileEvent.FileSavedSuccessfully, event)
                    3 -> assertEquals(SaveFileEvent.SaveFileError(testException), event)
                }
            }

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}