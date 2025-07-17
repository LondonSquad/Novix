package com.london.presentation.screen.details.tvshow.episodedetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.presentation.navigation.arguments.EpisodeDetailsArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.math.log

@KoinViewModel
class EpisodeDetailsViewModel(
    private val getTvShowImages: GetImagesById,
    private val getEpisodeByTvShowIdUseCase: GetEpisodeByTvShowId,
    private val getTvShowDetails: GetTvShowDetails,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args by lazy { EpisodeDetailsArgs(savedStateHandle) }

    private val _uiState = MutableStateFlow(EpisodeDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("TAG", ":${getTvShowImages(args.tvShowId)} ")
        }
        getEpisodeByTvShowId()
        getImagesData()
    }

    private fun getEpisodeByTvShowId() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                val episode = getEpisodeByTvShowIdUseCase(
                    args.tvShowId, args.seasonNumber, args.episodeNumber
                )
                uiState.copy(
                    tvShowId = args.tvShowId,
                    episodeNumber = args.episodeNumber,
                    seasonNumber = args.seasonNumber,
                    tvImages = listOf(),
                    episodeGenres = getTvShowDetails(args.tvShowId).tvShowGenres.map { it.name },
                    airDate = episode.airDate ?: "",
                    episodeTypes = episode.episodeTypes,
                    name = episode.name,
                    overview = episode.overview,
                    stillPath = episode.stillPath?: "",
                    voteAverage = episode.voteAverage,
                    voteCount = episode.voteCount,
                    guestStars = episode.guestStars,
                    id = 0,
                    backdropPath = "",
                )
            }
        }
    }

    private fun getImagesData() {
        viewModelScope.launch {
            val images = getTvShowImages(args.tvShowId)
            val episode = getEpisodeByTvShowIdUseCase(
                args.tvShowId, args.seasonNumber, args.episodeNumber
            )

            _uiState.update {
                it.copy(
                    tvImages = images,
                    guestStars = episode.guestStars
                )
            }
        }
    }
}
