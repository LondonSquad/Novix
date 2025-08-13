package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.actordetails.cast.ActorMediaItems
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopMoviesPicksViewModelTest {

    private lateinit var viewModel: TopMoviesPicksViewModel
    private lateinit var getActorMoviePicksById: GetActorMoviePicksByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        getActorMoviePicksById = mockk()
        savedStateHandle = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        with(viewModel.state.value) {
            assertThat(errorState).isNull()
        }
    }

    @Test
    fun `viewModel should implement all contract methods`() {
        // Given
        val actorId = 123
        val args = Screen.ActorTopMoviesPicksDetails(actorId)
        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns args
        coEvery { getActorMoviePicksById.invoke(actorId) } returns mockCastDetails

        // When
        viewModel = TopMoviesPicksViewModel(savedStateHandle, getActorMoviePicksById)

        // Then
        viewModel.onRetryClick()
        viewModel.onBackClick()
        viewModel.onSaveMovieClick(1)
        viewModel.onMovieClick(1)
    }

    private val mockCastDetails = ActorMediaDetails(
        mediaItems = listOf(
            ActorMediaItems(
                id = 1,
                posterUrl = "/test1.jpg"
            ),
            ActorMediaItems(
                id = 2,
                posterUrl = "/test2.jpg"
            )
        )
    )

}
