package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetEpisodesByTvShowSeason
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.domain.usecase.GetTvShowVideoProvider
import com.london.presentation.navigation.arguments.TvShowDetailsArgs
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

sealed interface TvShowDetailsEffect{
    data class OnNavigateToEpisodeDetails(val tvShowId: Int, val episodeNumber: Int, val seasonNumber: Int): TvShowDetailsEffect
}


@KoinViewModel
class TvShowDetailsViewModel(
    private val getTvShowDetails: GetTvShowDetails,
    private val getCastById: GetCastById,
    private val getTvShowImages: GetImagesById,
    private val getEpisodesByTvShowSeason: GetEpisodesByTvShowSeason,
    private val getTvShowVideoProvider: GetTvShowVideoProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TvShowDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TvShowDetailsEffect>()
    val effect = _effect.asSharedFlow()
    val args by lazy { TvShowDetailsArgs(savedStateHandle)}

    private val tvShowId: Int = args.tvShowId
    init {
        if (tvShowId != 0) {
            initializeGetTvShowDetailsData()
            initializeGetCastData()
            initializeGetImagesData()
            initializeEpisodesBySeasons()
        }
    }

    fun initializeEpisodesBySeasons(seasonNumber: Int = 1) {
        launchCatching {
            _uiState.update {
                it.copy(
                    tvShowEpisodes =
                        getEpisodesByTvShowSeason(tvShowId, seasonNumber).episodes,
                    tvShowEpisodeCountBySeason = getEpisodesByTvShowSeason(tvShowId,seasonNumber),
                    videoProvider = getTvShowVideoProvider.invoke(tvShowId).firstOrNull()?.videoUrl.orEmpty()
                )
            }
        }
    }

    private fun initializeGetImagesData() {
        launchCatching {
            val images = getTvShowImages(tvShowId)

            _uiState.update {
                it.copy(tvImages = images)
            }
        }
    }

    private fun initializeGetCastData() {
        launchCatching {
            _uiState.update {
                it.copy(cast = getCastById(tvShowId))
            }
        }
    }

    private fun initializeGetTvShowDetailsData() {
        launchCatching {
            _uiState.update {
                val tvShowDetails = getTvShowDetails(tvShowId)
                it.copy(
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
                )
            }
        }
    }

    fun onEpisodeClick(tvShowId: Int, episodeNumber: Int, seasonNumber: Int){
        launchCatching {
            _effect.emit(TvShowDetailsEffect.OnNavigateToEpisodeDetails(tvShowId, episodeNumber, seasonNumber))
        }
    }
}