package com.london.presentation.screen.details.tvshow.episodedetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.presentation.navigation.arguments.EpisodeDetailsArgs
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

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
        loadEpisodeDetails()
    }

    private fun loadEpisodeDetails() {
        launchCatching {
            val episode = getEpisodeByTvShowIdUseCase(
                args.tvShowId, args.seasonNumber, args.episodeNumber
            )
            val images = getTvShowImages(args.tvShowId)
            val tvShowDetails = getTvShowDetails(args.tvShowId)

            _uiState.update {
                EpisodeDetailsUiState(
                    tvShowId = args.tvShowId,
                    isLoading = false,
                    episodeNumber = args.episodeNumber,
                    seasonNumber = args.seasonNumber,
                    tvImages = images,
                    episodeGenres = tvShowDetails.tvShowGenres.map { it.name },
                    airDate = episode.airDate ?: "",
                    episodeTypes = episode.episodeTypes,
                    name = episode.name,
                    overview = episode.overview,
                    stillPath = episode.stillPath ?: "",
                    voteAverage = episode.voteAverage,
                    voteCount = episode.voteCount,
                    guestStars = episode.guestStars,
                    id = 0,
                    backdropPath = "",
                )
            }
        }
    }
}
