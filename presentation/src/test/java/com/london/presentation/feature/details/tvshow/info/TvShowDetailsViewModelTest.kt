package com.london.presentation.feature.details.tvshow.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetEpisodesByTvShowSeason
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.domain.usecase.rating.RatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
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
class TvShowDetailsViewModelTest {
    private lateinit var getCastById: GetCastById
    private lateinit var getTvShowImages: GetImagesById
    private lateinit var getEpisodesByTvShowSeason: GetEpisodesByTvShowSeason
    private lateinit var manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase
    private lateinit var manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase
    private lateinit var ratingUseCase: RatingUseCase
    private lateinit var authenticationUseCase: AuthenticationUseCase
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
        manageTvShowDetailsUseCase = mockk()
        getEpisodesByTvShowSeason = mockk()
        getTvShowImages = mockk()
        getCastById = mockk()

        every { savedStateHandle.getArgs<Screen.TvShowDetails>() } returns Screen.TvShowDetails(
            tvShowId = TV_SHOW_ID
        )
        coEvery { manageTvShowDetailsUseCase.getTvShowDetails(TV_SHOW_ID) } returns mockk(relaxed = true)
        coEvery { getCastById.invoke(TV_SHOW_ID) } returns mockk<TvShowCastEntity>(relaxed = true)
        coEvery { getEpisodesByTvShowSeason.invoke(TV_SHOW_ID, any()) } returns tvShowEpisodesEntity
        coEvery { getTvShowImages.invoke(TV_SHOW_ID) } returns emptyList()
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { manageRecentViewedUseCase.addToRecentViewed(any()) } returns Unit
        coEvery { manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(any()) } returns Unit
        coEvery { manageTvShowDetailsUseCase.getTvShowVideoProvider(TV_SHOW_ID) } returns emptyList()
        coEvery { ratingUseCase.getRateAccountTvShowState(TV_SHOW_ID) } returns 0

        viewModel = TvShowDetailsViewModel(
            getCastById = getCastById,
            getTvShowImages = getTvShowImages,
            getEpisodesByTvShowSeason = getEpisodesByTvShowSeason,
            manageTvShowDetailsUseCase = manageTvShowDetailsUseCase,
            manageRecentTvShowWatchedUseCase = manageRecentTvShowWatchedUseCase,
            manageRecentViewedUseCase = manageRecentViewedUseCase,
            ratingUseCase = ratingUseCase,
            authenticationUseCase = authenticationUseCase,
            savedStateHandle = savedStateHandle,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when initializeGetImagesData fails, error state should be updated`() = runTest {
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
    fun `When initializeEpisodesBySeasons fails, error state should be updated`() = runTest {
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
    fun `When initializeEpisodesBySeasons is called, videoProvider state should be updated`() =
        runTest {
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
    fun `When initializeGetTvShowDetailsData is called, tvShowDetails state should be updated`() =
        runTest {
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
            viewModel?.onReviewsClicked(TV_SHOW_ID, 1)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.NavigateToReviews(TV_SHOW_ID, 1)
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
    fun `OnGenreClicked should emit NavigateToTvShowsByCategoryId effect when clicked`() = runTest {
        // Given
        val genreId = 123

        // When & Then
        viewModel?.effect?.test {
            viewModel?.OnGenreClicked(genreId)
            assertThat(awaitItem()).isEqualTo(
                TvShowDetailsEffect.NavigateToTvShowsByCategoryId(genreId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRateBottomSheetClick should update error state when authentication fails`() = runTest {
        // Given
        coEvery { authenticationUseCase.isLoggedIn() } throws Exception("Authentication error")

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            viewModel?.onRateBottomSheetClick()
            val state = expectMostRecentItem()
            assertThat(state.error).isNotNull()
            ensureAllEventsConsumed()
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
        private val tvShowEpisodesEntity = mockk<TvShowEpisodesEntity>(relaxed = true)
    }
}