package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
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
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
    fun `viewModel should implement all contract methods`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.TopTvShowsPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns args
        coEvery { getActorTvShowPicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopTvShowsPicksViewModel(savedStateHandle, getActorTvShowPicksById)

        // Then
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(TopTvShowsPicksEffect.BackNavigation)

            viewModel.onTvShowClick(1)
            assertThat(awaitItem()).isEqualTo(TopTvShowsPicksEffect.TvShowDetailsNavigation(1))

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should show loading state when data is being fetched`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.TopTvShowsPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns args


        // When
        viewModel = TopTvShowsPicksViewModel(savedStateHandle, getActorTvShowPicksById)

        // Then
        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(TopTvShowsPicksUiState(isLoading = true))
            coEvery { getActorTvShowPicksById.invoke(actorId) } coAnswers {
                advanceTimeBy(100)
                mockCastDetails
            }
            assertThat(awaitItem()).isEqualTo(TopTvShowsPicksUiState(isLoading = false, actorTvShowDetails = mockCastDetails))
            cancelAndConsumeRemainingEvents()
        }
    }

    private val mockCastDetails = CastDetails(
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

}
