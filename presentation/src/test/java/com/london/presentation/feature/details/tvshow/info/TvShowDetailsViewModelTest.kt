package com.london.presentation.feature.details.tvshow.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.tvshowdetails.episode.SeasonEpisodes
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.domain.usecase.details.tvshow.GetTvEpisodesUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.genre.TvShowGenreUi
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
class TvShowDetailsViewModelTest {
    private lateinit var getTvEpisodesUseCase: GetTvEpisodesUseCase
    private lateinit var getTvShowUseCase: GetTvShowUseCase
    private lateinit var manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase
    private lateinit var ratingUseCase: ManageRatingUseCase
    private lateinit var authenticationUseCase: AuthenticationUseCase

    private lateinit var getActorUseCase: GetActorUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: TvShowDetailsViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        authenticationUseCase = mockk()
        ratingUseCase = mockk()
        manageRecentViewedUseCase = mockk()
        manageRecentTvShowWatchedUseCase = mockk()
        getTvShowUseCase = mockk()
        getTvEpisodesUseCase = mockk()
        getActorUseCase = mockk()

        every { savedStateHandle.getArgs<Screen.TvShowDetails>() } returns Screen.TvShowDetails(
            tvShowId = TV_SHOW_ID
        )
        coEvery { getActorUseCase.getActorTvShowPicksById(TV_SHOW_ID) } returns mockk<ActorMediaDetails>(
            relaxed = true
        )
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { manageRecentViewedUseCase.addToRecentViewed(any()) } returns Unit
        coEvery { manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(any()) } returns Unit
        coEvery { ratingUseCase.getRateAccountTvShowStatesById(TV_SHOW_ID) } returns 0

        viewModel = TvShowDetailsViewModel(
            getTvShowUseCase = getTvShowUseCase,
            manageRecentTvShowWatchedUseCase = manageRecentTvShowWatchedUseCase,
            manageRecentViewedUseCase = manageRecentViewedUseCase,
            ratingUseCase = ratingUseCase,
            authenticationUseCase = authenticationUseCase,
            savedStateHandle = savedStateHandle,
            getTvEpisodesUseCase = getTvEpisodesUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `error state should be updated, when initializeGetImagesData fails`() = runTest {
        // Given
        val exception = Exception("error")
        coEvery { getActorUseCase.getActorTvShowPicksById(TV_SHOW_ID) } throws exception

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `episodes by seasons data should be fetched, when initializeEpisodesBySeasons`() = runTest {
        // Given
        coEvery {
            getTvEpisodesUseCase.getTvShowSeasonEpisodes(
                TV_SHOW_ID,
                any()
            )
        } returns seasonEpisodes

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.tvShowEpisodes)
                .containsExactlyElementsIn(seasonEpisodes.episodes)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `error state should be updated, when initializeEpisodesBySeasons fails`() = runTest {

        // Given
        val exception = Exception("error")
        coEvery {
            getTvEpisodesUseCase.getTvShowSeasonEpisodes(
                TV_SHOW_ID,
                any()
            )
        } throws exception

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun ` videoProvider state should be updated, when initializeEpisodesBySeasons is called`() =
        runTest {
            // Given
            coEvery {
                getTvShowUseCase.getTvSeasonTrailer(
                    TV_SHOW_ID,
                    SEASON_NUMBER
                )
            } returns emptyList()

            // When
            advanceUntilIdle()

            // Then
            viewModel?.state?.test {
                val state = expectMostRecentItem()
                assertThat(state.videoProvider).isNotNull()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `tvShowDetails state should be updated, when initializeGetTvShowDetailsData is called`() =
        runTest {

            // Given
            coEvery { getTvShowUseCase.getTvShowDetails(TV_SHOW_ID) } returns mockk(
                relaxed = true
            )

            // When
            advanceUntilIdle()

            // Then
            viewModel?.state?.test {
                val state = expectMostRecentItem()
                assertThat(state.id).isNotNull()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `onEpisodeClicked should emit OnNavigateToEpisodeDetails effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onEpisodeClicked(TV_SHOW_ID, 1, 1)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.OnNavigateToEpisodeDetails(
                    tvShowId = TV_SHOW_ID,
                    episodeNumber = 1,
                    seasonNumber = 1
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onReviewsClicked should emit NavigateToReviews effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onReviewsClicked(TV_SHOW_ID, MediaType.TvShow)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.NavigateToReviews(TV_SHOW_ID, MediaType.TvShow)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCastClicked should emit NavigateToCast effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onCastClicked(TV_SHOW_ID)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.NavigateToCast(TV_SHOW_ID)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreClicked should emit NavigateToTvShowsByCategoryId effect when clicked`() = runTest {
        // Given
        val genre = TvShowGenreUi.ActionAdventure

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onGenreClicked(genre)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.NavigateToTvShowsByCategoryId(genre)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginClick should emit OnLoginNavigation effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onLoginClick()
            assertThat(awaitItem()).isEqualTo(TvShowDetailsEffect.OnLoginNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClicked should emit NavigateBack effect when clicked`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBackClicked()
            assertThat(awaitItem()).isEqualTo(TvShowDetailsEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val TV_SHOW_ID = 12345
        private const val SEASON_NUMBER = 1

        private val seasonEpisodes = mockk<SeasonEpisodes>(relaxed = true)
    }
}