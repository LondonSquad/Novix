package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.actordetails.cast.ActorMediaItems
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopMoviesPicksViewModelTest {

    private lateinit var getActorUseCase: GetActorUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: TopMoviesPicksViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        getActorUseCase = mockk()

        every { savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>() } returns Screen.ActorTopMoviesPicksDetails(
            actorId = ACTOR_ID
        )
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } returns mockCastDetails


        viewModel = TopMoviesPicksViewModel(
            savedStateHandle = savedStateHandle,
            getActorUseCase = getActorUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `onBackClick should emit BackNavigation effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBackClick()
            assertThat(awaitItem()).isEqualTo(TopMoviesPicksEffect.BackNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit MovieDetailsNavigation effect when clicked`() = runTest {
        // Given
        val movieId = 123

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onMovieClick(movieId)
            assertThat(awaitItem()).isEqualTo(
                TopMoviesPicksEffect.MovieDetailsNavigation(movieId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val ACTOR_ID = 123
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
}
