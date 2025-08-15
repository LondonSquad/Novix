package com.london.presentation.feature.details.actor.info.toptvshowspicks

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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopTvShowsPicksViewModelTest {

    private lateinit var getActorUseCase: GetActorUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: TopTvShowsPicksViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        getActorUseCase = mockk()

        every { savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns Screen.TopTvShowsPicksDetails(
            actorId = ACTOR_ID
        )
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns mockCastDetails


        viewModel = TopTvShowsPicksViewModel(
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
    fun `when getActorTvShowsPicksData fails, error state should be updated`() = runTest {
        // Given
        val exception = Exception("error")
        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } throws exception

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onBackClick should emit BackNavigation effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBackClick()
            assertThat(awaitItem()).isEqualTo(TopTvShowsPicksEffect.BackNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTvShowClick should emit TvShowDetailsNavigation effect when clicked`() = runTest {
        // Given
        val tvShowId = 123

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onTvShowClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(
                TopTvShowsPicksEffect.TvShowDetailsNavigation(tvShowId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

//    @Test
//    fun `onRetryClick should clear error state and fetch data again`() = runTest {
//        val testSavedStateHandle = mockk<SavedStateHandle>(relaxed = true)
//        every { testSavedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>() } returns Screen.TopTvShowsPicksDetails(
//            actorId = ACTOR_ID
//        )
//
//        val exception = Exception("error")
//        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } throws exception
//        coEvery { getActorUseCase.getActorTvShowPicksById(0) } throws exception
//
//        val testViewModel = TopTvShowsPicksViewModel(
//            savedStateHandle = testSavedStateHandle,
//            getActorUseCase = getActorUseCase
//        )
//        advanceUntilIdle()
//        testViewModel.state.test {
//            val errorState = expectMostRecentItem()
//            assertThat(errorState.errorState).isNotNull()
//            ensureAllEventsConsumed()
//        }
//        coEvery { getActorUseCase.getActorTvShowPicksById(ACTOR_ID) } returns mockCastDetails
//        coEvery { getActorUseCase.getActorTvShowPicksById(0) } returns mockCastDetails
//        testViewModel.onRetryClick()
//        advanceUntilIdle()
//
//        testViewModel.state.test {
//            val state = expectMostRecentItem()
//            assertThat(state.errorState).isNull()
//            assertThat(state.tvShowDetails).isEqualTo(mockCastDetails)
//            assertThat(state.isLoading).isFalse()
//            ensureAllEventsConsumed()
//        }
//        testViewModel.viewModelScope.cancel()
//    }

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