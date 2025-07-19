package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetEpisodesByTvShowSeason
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.domain.usecase.GetTvShowVideoProvider
import com.london.presentation.features.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TvShowDetailsViewModel(
    private val getTvShowDetails: GetTvShowDetails,
    private val getCastById: GetCastById,
    private val getTvShowImages: GetImagesById,
    private val getEpisodesByTvShowSeason: GetEpisodesByTvShowSeason,
    private val getTvShowVideoProvider: GetTvShowVideoProvider,
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
            block = { getTvShowDetails(tvShowId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { tvShowDetails ->
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
                        voteCount = tvShowDetails.voteCount
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        error = errorState
                    )
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { tvShowId != 0 }
        )
    }

    fun onEpisodeClick(tvShowId: Int, episodeNumber: Int, seasonNumber: Int) {
        emitEffect(
            TvShowDetailsEffect.OnNavigateToEpisodeDetails(
                tvShowId = tvShowId,
                episodeNumber = episodeNumber,
                seasonNumber = seasonNumber
            )
        )
    }

    override fun onEpisodeDetailsClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int) {
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

    override fun onBackClicked() {
        emitEffect(TvShowDetailsEffect.NavigateBack)
    }
}