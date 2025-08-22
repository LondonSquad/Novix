package com.london.presentation.feature.details.tvshow.info

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.entity.shared.MediaType
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.tvshow.GetTvEpisodesUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toUi
import com.london.presentation.utils.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val ratingUseCase: ManageRatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val getTvEpisodesUseCase: GetTvEpisodesUseCase,
    private val getTvShowUseCase: GetTvShowUseCase,
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<TvShowDetailsUiState, TvShowDetailsEffect>(TvShowDetailsUiState()),
    TvShowDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.TvShowDetails>()
    private val tvShowId: Int = args?.tvShowId.orZero()

    init {
        initializeGetTvShowDetailsData()
        initializeGetCastData()
        initializeGetImagesData()
        initializeEpisodesBySeasons()
    }

    fun initializeEpisodesBySeasons(seasonNumber: Int = 1) {
        tryToExecute(
            block = {
                val episodesBySeason = getTvEpisodesUseCase.getTvShowSeasonEpisodes(tvShowId, seasonNumber)
                val videoProvider = getTvShowUseCase.getTvSeasonTrailer(tvShowId, seasonNumber)
                Triple(episodesBySeason.episodes, episodesBySeason, videoProvider)
            },
            onSuccess = { (episodes, episodeCount, videoProviders) ->
                updateState {
                    copy(
                        tvShowEpisodes = episodes,
                        tvShowEpisodeCountBySeason = episodeCount,
                        videoProvider = videoProviders.firstOrNull().orEmpty(),
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    fun onRetry() {
        setLoadingState(null)
        initializeGetTvShowDetailsData()
        initializeGetCastData()
        initializeGetImagesData()
        initializeEpisodesBySeasons()
    }

    override fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int) {
        clearRatedState()
        emitEffect(
            TvShowDetailsEffect.OnNavigateToEpisodeDetails(
                tvShowId = tvShowId,
                episodeNumber = episodeNumber,
                seasonNumber = seasonNumber
            )
        )
    }

    override fun onReviewsClicked(tvShowId: Int, mediaType: MediaType) {
        clearRatedState()
        emitEffect(TvShowDetailsEffect.NavigateToReviews(tvShowId, mediaType))
    }

    override fun onCastClicked(tvShowId: Int) {
        clearRatedState()
        emitEffect(TvShowDetailsEffect.NavigateToCast(tvShowId))
    }

    override fun onGenreClicked(genre: TvShowGenreUi) {
        clearRatedState()
        emitEffect(TvShowDetailsEffect.NavigateToTvShowsByCategoryId(genre))
    }

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn ->
                if (isLoggedIn) updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
                else
                    updateState {
                        copy(
                            isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not(),
                            isGuestUser = true
                        )
                    }
            },
            onError = ::setErrorState
        )
    }

    override fun onSelectRatingClick(rating: Int) {
        tryToExecute(
            block = {
                ratingUseCase.addTvShowRatingById(tvShowId, rating)
            },
            onSuccess = {
                updateState {
                    copy(
                        isRateBottomSheetVisible = false,
                        isSuccessfullyRated = true,
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        error = errorState,
                        isSuccessfullyRated = false
                    )
                }
            },
            onCompleted = { setLoadingState(false) },
        )
    }

    override fun onLoginClick(tvShowId: Int) = emitEffect(TvShowDetailsEffect.OnLoginNavigation(tvShowId))

    override fun onBackClicked() = emitEffect(TvShowDetailsEffect.NavigateBack)
    

    private fun initializeGetImagesData() {

        tryToExecute(
            block = { getTvShowUseCase.getImagesTvShowById(tvShowId) },
            onStart = { setLoadingState(true) },
            onSuccess = { images -> updateState { copy(tvImages = images) } },
            onError = ::setErrorState,
            onCompleted = { setLoadingState(false) },
        )
    }

    private fun initializeGetCastData() {
        tryToExecute(
            block = { getTvShowUseCase.getTvShowCastById(tvShowId) },
            onStart = { setLoadingState(true) },
            onSuccess = { cast ->
                updateState {
                    copy(
                        cast = cast,
                        isLoading = false
                    )
                }
            },
            onError = { errorState -> setErrorState(errorState);setLoadingState(false) },
            onCompleted = { setLoadingState(false) },
        )
    }

    private fun initializeGetTvShowDetailsData() {
        tryToExecute(
            block = {
                val tvShowDetails = getTvShowUseCase.getTvShowDetails(tvShowId)

                val firstSeason = tvShowDetails.tvShowSeasons.firstOrNull()
                val seasonNumber = firstSeason ?: 1

                val episodes = getTvEpisodesUseCase.getTvShowSeasonEpisodes(
                    tvShowId,
                    seasonNumber
                ).episodes
                val rating = if (authenticationUseCase.isLoggedIn()) {
                    ratingUseCase.getRateAccountTvShowStatesById(
                        id = tvShowId,
                    )
                } else 0

                Triple(tvShowDetails, rating, episodes)
            },
            onStart = { setLoadingState(true) },
            onSuccess = { (tvShowDetails, rating, episodes) ->
                updateState {
                    copy(
                        firstAirDate = tvShowDetails.firstAirDate,
                        tvShowGenres = tvShowDetails.tvShowGenres.map { it.toUi() },
                        id = tvShowDetails.id,
                        name = tvShowDetails.name,
                        numberOfSeasons = tvShowDetails.numberOfSeasons,
                        overview = tvShowDetails.overview,
                        voteAverage = tvShowDetails.voteAverage,
                        tvShowEpisodes = episodes,
                        isRated = rating != 0 && state.value.isGuestUser.not(),
                    )
                }
                addMovieToRecentViewed(
                    RecentViewed(
                        id = tvShowDetails.id,
                        imageUrl = tvShowDetails.posterUrl.toString(),
                        type = MediaType.TvShow,
                        viewDate = System.currentTimeMillis()
                    )
                )
                addToRecentWatched(
                    TvShow(
                        id = tvShowDetails.id,
                        name = tvShowDetails.name,
                        posterPicture = tvShowDetails.posterUrl.toString(),
                        releaseYear = 2025,
                        rating = 1,
                        genres = tvShowDetails.tvShowGenres.map { it },
                    )
                )
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { setLoadingState(false) },
        )
    }

    private suspend fun addToRecentWatched(tvShow: TvShow) =
        manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(tvShow)

    private suspend fun addMovieToRecentViewed(tvShow: RecentViewed) =
        manageRecentViewedUseCase.addToRecentViewed(tvShow)

    private fun clearRatedState() = updateState { copy(isSuccessfullyRated = null) }

    private fun setLoadingState(loading: Boolean?) = updateState { copy(isLoading = loading ?: false) }

    private fun setErrorState(errorState: ErrorState) = updateState { copy(error = errorState) }
}
