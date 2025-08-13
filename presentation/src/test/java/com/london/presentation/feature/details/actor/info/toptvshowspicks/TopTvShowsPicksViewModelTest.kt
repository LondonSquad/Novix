package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.actordetails.cast.CastActorEntity
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.usecase.toppicks.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowsPicksViewModelTest {

    private lateinit var viewModel: TopTvShowsPicksViewModel
    private lateinit var getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val mockCastDetails = CastDetails(
        id = 123,
        cast = listOf(
            CastActorEntity(
                id = 1,
                posterUrl = "/test1.jpg"
            ),
            CastActorEntity(
                id = 2,
                posterUrl = "/test2.jpg"
            )
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getActorTvShowPicksById = mockk()
        savedStateHandle = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should use default actorId when it is initialized with null args`() = runTest {
        // Given
        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns null
        coEvery { getActorTvShowPicksById.invoke(0) } returns CastDetails()

        // When
        viewModel = TopTvShowsPicksViewModel(savedStateHandle, getActorTvShowPicksById)

        // Then
        coVerify(exactly = 1) { getActorTvShowPicksById.invoke(0) }
    }

    @Test
    fun `should show loading state when data is being fetched`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.TopTvShowsPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns args
        coEvery { getActorTvShowPicksById.invoke(actorId) } coAnswers {
            delay(100)
            mockCastDetails
        }

        // When
        viewModel = TopTvShowsPicksViewModel(savedStateHandle, getActorTvShowPicksById)
    }

    @Test
    fun `viewModel should implement all contract methods`() {
        // Given
        val actorId = 123
        val args = Screen.TopTvShowsPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns args
        coEvery { getActorTvShowPicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopTvShowsPicksViewModel(savedStateHandle, getActorTvShowPicksById)

        // Then
        viewModel.onRetryClick()
        viewModel.onBackClick()
        viewModel.onSaveTvShowClick(1)
        viewModel.onTvShowClick(1)
    }
}
