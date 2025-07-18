package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetEpisodesByTvShowSeason
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.presentation.navigation.arguments.TvShowDetailsArgs
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TvShowDetailsInteractionListener {

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
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tvShowEpisodes =
                        getEpisodesByTvShowSeason(tvShowId, seasonNumber).episodes,
                    tvShowEpisodeCountBySeason = getEpisodesByTvShowSeason(tvShowId,seasonNumber),
                )
            }
        }
    }

    private fun initializeGetImagesData() {
        viewModelScope.launch {
            val images = getTvShowImages(tvShowId)

            _uiState.update {
                it.copy(tvImages = images)
            }
        }
    }

    private fun initializeGetCastData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(cast = getCastById(tvShowId))
            }
        }
    }

    private fun initializeGetTvShowDetailsData() {
        viewModelScope.launch {
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
        viewModelScope.launch {
            _effect.emit(TvShowDetailsEffect.OnNavigateToEpisodeDetails(tvShowId, episodeNumber, seasonNumber))
        }
    }

    override fun onClickViewReviewsListener(tvShowId: Int) {
        // TODO (should navigate to reviews screen)
    }
}