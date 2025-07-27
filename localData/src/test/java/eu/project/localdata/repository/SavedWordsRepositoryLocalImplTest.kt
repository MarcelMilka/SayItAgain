package eu.project.localdata.repository

import app.cash.turbine.test
import eu.project.common.localData.SavedWordsDataSource
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class SavedWordsRepositoryLocalImplTest {

    val testDispatcher = StandardTestDispatcher()
    val ioCoroutineScope = CoroutineScope(testDispatcher)

    private lateinit var savedWordsRepositoryLocalImpl: SavedWordsRepositoryLocalImpl
    private lateinit var savedWordsDataSource: SavedWordsDataSource

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

        savedWordsDataSource = mockk()
    }

    @After
    fun tearDown() {

        testDispatcher.cancel()
        ioCoroutineScope.cancel()
    }



    @Test
    fun `saveWord updates dataState from Loaded_NoData to Loaded_Data`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>()
        val secondRetrievedData = listOf<SavedWord>(firstEntityInstance)

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData) andThen Result.success(secondRetrievedData)
        coEvery {savedWordsDataSource.saveWord(any()) } returns Result.success(Unit)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.saveWord(firstEntityInstance)

            // on call saveWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(secondRetrievedData),
                actual = this.awaitItem()
            )
        }
    }

    @Test
    fun `saveWord updates dataState from Loaded_NoData to FailedToLoad on failure`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>()
        val secondRetrievedData = listOf<SavedWord>(firstEntityInstance)

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData) // andThen Result.success(secondRetrievedData)
        coEvery {savedWordsDataSource.saveWord(any()) } returns Result.failure(Throwable("SaveWord onFailure"))

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.saveWord(firstEntityInstance)

            // on call saveWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.FailedToLoad("SaveWord onFailure"),
                actual = this.awaitItem()
            )
        }
    }

    @Test
    fun `saveWord updates dataState from Loaded_Data to Loaded_Data including new word`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>(firstEntityInstance)
        val secondRetrievedData = listOf<SavedWord>(firstEntityInstance, secondEntityInstance)

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData) andThen Result.success(secondRetrievedData)
        coEvery {savedWordsDataSource.saveWord(any()) } returns Result.success(Unit)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(firstRetrievedData),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.saveWord(secondEntityInstance)

            // on call saveWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(secondRetrievedData),
                actual = this.awaitItem()
            )
        }
    }

    @Test
    fun `saveWord updates dataState from Loaded_Data to FailedToLoad on failure`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>(firstEntityInstance)

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData)
        coEvery {savedWordsDataSource.saveWord(any()) } returns Result.failure(Throwable("SaveWord onFailure"))

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(firstRetrievedData),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.saveWord(secondEntityInstance)

            // on call saveWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.FailedToLoad("SaveWord onFailure"),
                actual = this.awaitItem()
            )
        }
    }



    @Test
    fun `deleteWord updates dataState from Loaded_Data to Loaded_NoData`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>(firstEntityInstance)
        val secondRetrievedData = listOf<SavedWord>()

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData) andThen Result.success(secondRetrievedData)
        coEvery {savedWordsDataSource.deleteWord(any()) } returns Result.success(Unit)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(firstRetrievedData),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.deleteWord(firstEntityInstance)

            // on call deleteWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )
        }
    }

    @Test
    fun `deleteWord updates dataState from Loaded_Data to Loaded_Data`() = runTest(testDispatcher) {

        // stub
        val firstRetrievedData = listOf<SavedWord>(firstEntityInstance, secondEntityInstance)
        val secondRetrievedData = listOf<SavedWord>(firstEntityInstance)

        coEvery {savedWordsDataSource.getAllWords() } returns Result.success(firstRetrievedData) andThen Result.success(secondRetrievedData)
        coEvery {savedWordsDataSource.deleteWord(any()) } returns Result.success(Unit)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(firstRetrievedData),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.deleteWord(firstEntityInstance)

            // on call deleteWord
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(secondRetrievedData),
                actual = this.awaitItem()
            )
        }
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on init returns FailedToLoadData on Result failure`() = runTest(testDispatcher) {

        // stub
        coEvery { savedWordsDataSource.getAllWords() } returns Result.failure(Throwable(message = "Exemplary message"))

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.FailedToLoad(cause = "Exemplary message"),
                actual = this.awaitItem()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on call returns FailedToLoadData on Result failure`() = runTest(testDispatcher) {

        // stub
        coEvery { savedWordsDataSource.getAllWords() } returns Result.failure(Throwable(message = "Exemplary message"))

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.FailedToLoad(cause = "Exemplary message"),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.loadAllWords()

            // on call
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.FailedToLoad(cause = "Exemplary message"),
                actual = this.awaitItem()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on init returns Loaded NoData on Result success empty list`() = runTest(testDispatcher) {

        // stub
        val retrievedData = listOf<SavedWord>()
        coEvery { savedWordsDataSource.getAllWords() } returns Result.success(retrievedData)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on call returns Loaded NoData on Result success empty list`() = runTest(testDispatcher) {

        // stub
        val retrievedData = listOf<SavedWord>()
        coEvery { savedWordsDataSource.getAllWords() } returns Result.success(retrievedData)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.loadAllWords()

            // on call
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.NoData,
                actual = this.awaitItem()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on init returns Loaded NoData on Result success list with SavedWord instances`() = runTest(testDispatcher) {

        // stub
        val retrievedData = listOf<SavedWord>(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
        coEvery { savedWordsDataSource.getAllWords() } returns Result.success(retrievedData)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(retrievedData),
                actual = this.awaitItem()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadAllWords on call returns Loaded NoData on Result success list with SavedWord instances`() = runTest(testDispatcher) {

        // stub
        val retrievedData = listOf<SavedWord>(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
        coEvery { savedWordsDataSource.getAllWords() } returns Result.success(retrievedData)

        // run
        savedWordsRepositoryLocalImpl = SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )

        // test
        savedWordsRepositoryLocalImpl.dataState.test {

            // on init
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(retrievedData),
                actual = this.awaitItem()
            )

            // call
            savedWordsRepositoryLocalImpl.loadAllWords()

            // on call
            assertEquals(
                expected = SavedWordsRepositoryDataState.Loading,
                actual = this.awaitItem()
            )

            assertEquals(
                expected = SavedWordsRepositoryDataState.Loaded.Data(retrievedData),
                actual = this.awaitItem()
            )
        }
    }
}