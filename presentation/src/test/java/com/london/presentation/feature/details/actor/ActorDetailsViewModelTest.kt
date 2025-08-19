package com.london.presentation.feature.details.actor

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actor.ActorDetails
import com.london.domain.entity.actor.cast.ActorMediaDetails
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorDetailsViewModelTest {

    private lateinit var viewModel: ActorDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var getActorUseCase: GetActorUseCase 
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getActorUseCase = mockk()
        savedStateHandle = mockk(relaxed = true)
        
        every { savedStateHandle.getArgs<Screen.ActorDetails>() } returns Screen.ActorDetails(
            actorId = ACTOR_ID
        )
        
        coEvery { getActorUseCase.getActorDetailsById(ACTOR_ID) } returns ActorDetails()
        coEvery { getActorUseCase.getActorImagesById(ACTOR_ID) } returns emptyList()
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } returns ActorMediaDetails()
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns ActorMediaDetails()
        
        viewModel = ActorDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getActorUseCase = getActorUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should handle actor details loading error`() = runTest {
        // Given
        val mockError = RuntimeException("Network error")
        coEvery { getActorUseCase.getActorDetailsById(ACTOR_ID) } throws mockError
        coEvery { getActorUseCase.getActorImagesById(ACTOR_ID) } returns emptyList()
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } returns ActorMediaDetails()
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns ActorMediaDetails()

        // When
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertThat(finalState.error).isNotNull()
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `should handle actor images loading error`() = runTest {
        // Given
        val mockError = RuntimeException("Image loading error")
        coEvery { getActorUseCase.getActorDetailsById(ACTOR_ID) } returns ActorDetails()
        coEvery { getActorUseCase.getActorImagesById(ACTOR_ID) } throws mockError
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } returns ActorMediaDetails()
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns ActorMediaDetails()

        // When
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertThat(finalState.error).isNotNull()
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `should handle movie picks loading error`() = runTest {
        // Given
        val mockError = RuntimeException("Movie loading error")
        coEvery { getActorUseCase.getActorDetailsById(ACTOR_ID) } returns ActorDetails()
        coEvery { getActorUseCase.getActorImagesById(ACTOR_ID) } returns emptyList()
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } throws mockError
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns ActorMediaDetails()

        // When
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertThat(finalState.error).isNotNull()
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `should handle tv show picks loading error`() = runTest {
        // Given
        coEvery { getActorUseCase.getActorDetailsById(ACTOR_ID) } returns ActorDetails()
        coEvery { getActorUseCase.getActorImagesById(ACTOR_ID) } returns emptyList()
        coEvery { getActorUseCase.getActorMoviePicksById(ACTOR_ID) } returns ActorMediaDetails()
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns ActorMediaDetails()

        // When
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertThat(finalState.error).isNotNull()
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `should emit back navigation effect when onBackClick is called`() = runTest {
        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(ActorEffect.BackNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit gallery navigation effect when onActorGalleryClick is called`() = runTest {
        // When & Then
        viewModel.effect.test {
            viewModel.onActorGalleryClick(ACTOR_ID)
            assertThat(awaitItem()).isEqualTo(ActorEffect.GalleryNavigation(ACTOR_ID))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit top movie picks navigation effect when onTopMoviePicksClick is called`() = runTest {
        // When & Then
        viewModel.effect.test {
            viewModel.onTopMoviePicksClick(ACTOR_ID)
            assertThat(awaitItem()).isEqualTo(ActorEffect.TopMoviePicksNavigation(ACTOR_ID))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit movie screen navigation effect when onMovieScreenClick is called`() = runTest {
        // Given
        val movieId = 123

        // When & Then
        viewModel.effect.test {
            viewModel.onMovieScreenClick(movieId)
            assertThat(awaitItem()).isEqualTo(ActorEffect.MovieScreenNavigation(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit top tv show picks navigation effect when onTopTvShowPicksClick is called`() = runTest {
        // When & Then
        viewModel.effect.test {
            viewModel.onTopTvShowPicksClick(ACTOR_ID)
            assertThat(awaitItem()).isEqualTo(ActorEffect.TopTvShowPicksNavigation(ACTOR_ID))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit tv show screen navigation effect when onTvShowScreenClick is called`() = runTest {
        // Given
        val tvShowId = 456

        // When & Then
        viewModel.effect.test {
            viewModel.onTvShowScreenClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(ActorEffect.TvShowScreenNavigation(tvShowId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val ACTOR_ID = 123
    }

}
