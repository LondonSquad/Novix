package com.london.presentation.feature.details.tvshow.tvshowdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.AddTvShowRatingByIdUseCase
import com.london.domain.usecase.GetAccountTvShowStateUseCase
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetEpisodesByTvShowSeason
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.domain.usecase.GetTvShowVideoProvider
import com.london.domain.usecase.LoggedInUseCase
import com.london.domain.usecase.recent.viewed.AddToRecentViewedUseCase
import com.london.domain.usecase.recent.watched.AddTvShowToRecentWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val getTvShowDetails: GetTvShowDetails,
    private val getCastById: GetCastById,
    private val getTvShowImages: GetImagesById,
    private val getEpisodesByTvShowSeason: GetEpisodesByTvShowSeason,
    private val getTvShowVideoProvider: GetTvShowVideoProvider,
    private val addTvShowToRecentWatchedUseCase:AddTvShowToRecentWatchedUseCase,
    private val addToRecentViewedUseCase:AddToRecentViewedUseCase,
    private val addTvShowRatingByIdUseCase: AddTvShowRatingByIdUseCase,
    private val getUserLoggedInUseCase: LoggedInUseCase,
    private val getAccountTvShowStateUseCase: GetAccountTvShowStateUseCase,
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
                val episodesBySeason = getEpisodesByTvShowSeason(tvShowId, seasonNumber)
                val videoProvider = getTvShowVideoProvider.invoke(tvShowId)
                Triple(episodesBySeason.episodes, episodesBySeason, videoProvider)
            },
            onSuccess = { (episodes, episodeCount, videoProviders) ->
                updateState {
                    copy(
                        tvShowEpisodes = episodes,
                        tvShowEpisodeCountBySeason = episodeCount,
                        videoProvider = videoProviders.firstOrNull()?.videoUrl.orEmpty()
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { tvShowId != 0 }
        )
    }

    private fun initializeGetImagesData() {

        tryToExecute(
            block = {
                getTvShowImages(tvShowId)
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
            checkSuccess = { tvShowId != 0 },
        )
    }

    private fun initializeGetCastData() {
        tryToExecute(
            block = {
                getCastById(tvShowId)
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
            checkSuccess = { tvShowId != 0 }
        )
    }

    private fun initializeGetTvShowDetailsData() {
        tryToExecute(
            block = {
                val tvShowDetails = getTvShowDetails(tvShowId)

                val firstSeason = tvShowDetails.tvShowSeasons.firstOrNull()
                val seasonNumber = firstSeason?.seasonNumber ?: 1

                val episodes = getEpisodesByTvShowSeason(tvShowId, seasonNumber).episodes
                val rating = if (getUserLoggedInUseCase.invoke()) {
                    getAccountTvShowStateUseCase.invoke(
                        tvShowId = tvShowId,
                    )
                } else 0

                Triple(tvShowDetails, rating, episodes)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { (tvShowDetails, rating, episodes) ->
                Log.d("test", "initializeGetTvShowDetailsData: $rating")
                updateState {
                    copy(
                        adult = tvShowDetails.adult,
                        backdropPath = tvShowDetails.backdropUrl,
                        createdBy = tvShowDetails.createdBy,
                        episodeRunTime = tvShowDetails.episodeRunTime,
                        firstAirDate = tvShowDetails.firstAirDate,
                        tvShowGenres = tvShowDetails.tvShowGenres,
                        homepage = tvShowDetails.homepage,
                        id = tvShowDetails.id,
                        inProduction = tvShowDetails.inProduction,
                        languages = tvShowDetails.languages,
                        lastAirDate = tvShowDetails.lastAirDate,
                        lastTvShowEpisodeToAir = tvShowDetails.lastTvShowEpisodeToAir,
                        name = tvShowDetails.name,
                        nextTvShowEpisodeToAir = tvShowDetails.nextTvShowEpisodeToAir,
                        tvShowNetworks = tvShowDetails.tvShowNetworks,
                        numberOfEpisodes = tvShowDetails.numberOfEpisodes,
                        numberOfSeasons = tvShowDetails.numberOfSeasons,
                        originCountry = tvShowDetails.originCountry,
                        originalLanguage = tvShowDetails.originalLanguage,
                        originalName = tvShowDetails.originalName,
                        overview = tvShowDetails.overview,
                        popularity = tvShowDetails.popularity,
                        posterPath = tvShowDetails.posterUrl,
                        productionCompanies = tvShowDetails.productionCompanies,
                        productionCountries = tvShowDetails.productionCountries,
                        tvShowSeasons = tvShowDetails.tvShowSeasons,
                        tvShowSpokenLanguages = tvShowDetails.tvShowSpokenLanguageEntities,
                        status = tvShowDetails.status,
                        tagline = tvShowDetails.tagline,
                        type = tvShowDetails.type,
                        voteAverage = tvShowDetails.voteAverage,
                        voteCount = tvShowDetails.voteCount,
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
                        genres = tvShowDetails.tvShowGenres.map { it.id },
                    )
                )
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { tvShowId != 0 }
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

    override fun onReviewsClicked(tvShowId: Int, mediaType: Int) {
        emitEffect(TvShowDetailsEffect.NavigateToReviews(tvShowId, mediaType))
    }

    override fun onCastClicked(tvShowId: Int) {
        emitEffect(TvShowDetailsEffect.NavigateToCast(tvShowId))
    }

    override fun OnGenreClicked(genreId: Int) {
        emitEffect(TvShowDetailsEffect.NavigateTotvShowsByCategoryId(genreId))
    }

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { getUserLoggedInUseCase.invoke() },
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
                addTvShowRatingByIdUseCase.invoke(tvShowId, rating)
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
    private suspend fun addToRecentWatched(tvShow: TvShow){
        addTvShowToRecentWatchedUseCase.invoke(tvShow)
    }
    private suspend fun addMovieToRecentViewed(tvShow: RecentViewed){
        addToRecentViewedUseCase.invoke(tvShow)
    }
}