package eu.project.floatingActionButton.vm

import app.cash.turbine.test
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.common.navigation.Screens
import eu.project.floatingActionButton.model.FloatingActionButtonViewState
import eu.project.floatingActionButton.model.FloatingActionButtonVisibilityState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

class FloatingActionButtonViewModelTest {

    private lateinit var savedWordsRepository: SavedWordsRepository
    private lateinit var viewModel: FloatingActionButtonViewModel

    private val firstInstance = SavedWord(
        uuid = UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"),
        word = "Cat",
        language = "English"
    )
    private val secondInstance = SavedWord(
        uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        word = "Monitor lizard",
        language = "English"
    )
    private val thirdInstance = SavedWord(
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        word = "Hagetreboa",
        language = "Norwegian"
    )

    private val retrievedData = listOf(firstInstance, secondInstance, thirdInstance)

    @Before
    fun setUp() {

        savedWordsRepository = mockk(relaxed = true)
    }



    // onRouteChanged & currentScreen tests
    @Test
    fun `onRouteChanged properly updates currentScreen`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(SavedWordsRepositoryDataState.Loading)

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.currentScreen.test {

            // Screens.HOME by default
            assertEquals(
                Screens.HOME,
                this.awaitItem()
            )

            // simulate navigating to Screens.SAVED_WORDS
            viewModel.onRouteChanged(Screens.SAVED_WORDS)

            assertEquals(
                Screens.SAVED_WORDS,
                this.awaitItem()
            )

            // simulate navigating to Screens.EXPORT_WORDS
            viewModel.onRouteChanged(Screens.EXPORT_WORDS)

            assertEquals(
                Screens.EXPORT_WORDS,
                this.awaitItem()
            )

            this.expectNoEvents()
        }
    }



    // visibilityState tests
    @Test
    fun `visibilityState is hidden by default`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(SavedWordsRepositoryDataState.Loading)

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())
        }
    }

    @Test
    fun `visibilityState is hidden when screen is null`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(
            SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
        )

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(null)

            this.expectNoEvents()
        }
    }

    @Test
    fun `visibilityState is hidden when screen is not SAVED_WORDS`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(
            SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
        )

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(Screens.SELECT_AUDIO_SCREEN)

            this.expectNoEvents()
        }
    }

    @Test
    fun `visibilityState is visible when screen is SAVED_WORDS and dataState is Data`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(
            SavedWordsRepositoryDataState.Loaded.Data(retrievedData)
        )

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(Screens.SAVED_WORDS)

            assertEquals(FloatingActionButtonVisibilityState.Visible, awaitItem())
        }
    }

    @Test
    fun `visibilityState is hidden when screen is SAVED_WORDS and dataState is NoData`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(
            SavedWordsRepositoryDataState.Loaded.NoData
        )

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(Screens.SAVED_WORDS)

            this.expectNoEvents()
        }
    }

    @Test
    fun `visibilityState is hidden when screen is SAVED_WORDS and dataState is FailedToLoad`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(
            SavedWordsRepositoryDataState.FailedToLoad("Error")
        )

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(Screens.SAVED_WORDS)

            this.expectNoEvents()
        }
    }

    @Test
    fun `visibilityState is hidden when screen is SAVED_WORDS and dataState is Loading`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(SavedWordsRepositoryDataState.Loading)

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.visibilityState.test {
            assertEquals(FloatingActionButtonVisibilityState.Hidden, awaitItem())

            viewModel.onRouteChanged(Screens.SAVED_WORDS)

            this.expectNoEvents()
        }
    }



    // viewState tests
    @Test
    fun `viewState is default by default`() = runTest {

        // stub
        every { savedWordsRepository.dataState } returns MutableStateFlow(SavedWordsRepositoryDataState.Loading)

        // init
        viewModel = FloatingActionButtonViewModel(savedWordsRepository)

        // test
        viewModel.viewState.test {
            assertEquals(FloatingActionButtonViewState(), awaitItem())
        }
    }
}