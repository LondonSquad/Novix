package com.london.presentation.screen.details.tvshow.tvshowdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.GetCastById
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TvShowDetailsViewModel(
    private val getTvShowDetails: GetTvShowDetails,
    private val getCastById: GetCastById,
    private val getTvShowImages: GetImagesById,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TvShowDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val tvShowId: Int = savedStateHandle.get<Int>("tvShowId") ?: 0

    init {
        Log.d("TAG", ":$tvShowId ")
        if (tvShowId != 0) {
            getTvShowDetailsData()
            getCastData()
            getImagesData()
        }
    }

    private fun getImagesData() {
        viewModelScope.launch {
            val images = getTvShowImages(tvShowId)

            _uiState.update {
                it.copy(tvImages = images.backdrops)
            }
        }
    }

    private fun getCastData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(cast = getCastById(tvShowId))
            }
        }
    }

    private fun getTvShowDetailsData() {
        viewModelScope.launch {
            _uiState.update {
                val tvShowDetails = getTvShowDetails(tvShowId)
                it.copy(
                    adult = tvShowDetails.adult,
                    backdropPath = tvShowDetails.backdropPath,
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
                    posterPath = tvShowDetails.posterPath,
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
}