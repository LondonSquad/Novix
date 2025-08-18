package com.london.presentation.feature.details.tvshow.info

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.tvshow.GetTvEpisodesUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toUi
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
    private val tvShowId: Int = args?.tvShowId ?: 0

    init {
        initializeGetTvShowDetailsData()
        initializeGetCastData()
        initializeGetImagesData()
        initializeEpisodesBySeasons()
    }

    fun initializeEpisodesBySeasons(seasonNumber: Int = 1) {
        tryToExecute(
            block = {
                val episodesBySeason =
                    getTvEpisodesUseCase.getTvShowEpisodesBySeason(tvShowId, seasonNumber)
                val videoProvider = getTvShowUseCase.getTvShowVideo(tvShowId)
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
        updateState { copy(error = null) }
        initializeGetTvShowDetailsData()
        initializeGetCastData()
        initializeGetImagesData()
        initializeEpisodesBySeasons()
    }

    override fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int) {
        emitEffect(
            TvShowDetailsEffect.OnNavigateToEpisodeDetails(
                tvShowId = tvShowId,
                episodeNumber = episodeNumber,
                seasonNumber = seasonNumber
            )
        )
    }

    override fun onReviewsClicked(tvShowId: Int, mediaType: MediaType) {
        emitEffect(TvShowDetailsEffect.NavigateToReviews(tvShowId, mediaType))
    }

    override fun onCastClicked(tvShowId: Int) {
        emitEffect(TvShowDetailsEffect.NavigateToCast(tvShowId))
    }

    override fun onGenreClicked(genre: TvShowGenreUi) {
        emitEffect(TvShowDetailsEffect.NavigateToTvShowsByCategoryId(genre))
    }

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn ->
                if (isLoggedIn)
                    updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
                else
                    updateState {
                        copy(
                            isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not(),
                            isGuestUser = true
                        )

                    }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            }
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
                        selectedRating = rating,
                        isRateBottomSheetVisible = false,
                        isSuccessfullyRated = true,
                        isRated = true
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
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onLoginClick() = emitEffect(TvShowDetailsEffect.OnLoginNavigation)

    override fun onBackClicked() {
        emitEffect(TvShowDetailsEffect.NavigateBack)
    }

    private fun initializeGetImagesData() {

        tryToExecute(
            block = {
                getTvShowUseCase.getImagesTvShowById(tvShowId)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { images ->
                updateState {
                    copy(
                        tvImages = images,
                    )
                }
            },
            onError = { error -> updateState { copy(error = error) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun initializeGetCastData() {
        tryToExecute(
            block = {
                getTvShowUseCase.getTvShowCastById(tvShowId)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { cast ->
                updateState {
                    copy(
                        cast = cast,
                        isLoading = false
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        isLoading = false,
                        error = errorState
                    )
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun initializeGetTvShowDetailsData() {
        tryToExecute(
            block = {
                val tvShowDetails = getTvShowUseCase.getTvShowDetails(tvShowId)

                val firstSeason = tvShowDetails.tvShowSeasons.firstOrNull()
                val seasonNumber = firstSeason ?: 1

                val episodes = getTvEpisodesUseCase.getTvShowEpisodesBySeason(
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
            onStart = { updateState { copy(isLoading = true) } },
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
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private suspend fun addToRecentWatched(tvShow: TvShow) =
        manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(tvShow)

    private suspend fun addMovieToRecentViewed(tvShow: RecentViewed) =
        manageRecentViewedUseCase.addToRecentViewed(tvShow)
}
