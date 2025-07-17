package com.london.presentation.screen.details.tvshow.episodedetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetImagesById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EpisodeDetailsViewModel(
    private val getTvShowImages: GetImagesById,
    private val getEpisodeByTvShowIdUseCase: GetEpisodeByTvShowId,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EpisodeDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val tvShowId: Int = savedStateHandle.get<Int>("tvShowId") ?: 0
    private val seasonNumber: Int = savedStateHandle.get<Int>("seasonNumber") ?: 0
    private val episodeNumber: Int = savedStateHandle.get<Int>("episodeNumber") ?: 0

    init {
        Log.d(
            "TAG",
            "tvshowId:$tvShowId, seasonNumber:$seasonNumber, episodeNumber:$episodeNumber "
        )
        if (tvShowId != 0 && seasonNumber != 0 && episodeNumber != 0) {
            getImagesData()
            getEpisodeByTvShowId(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )
        }
    }

    private fun getEpisodeByTvShowId(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tvShowEpisode =
                        getEpisodeByTvShowIdUseCase(
                            tvShowId = tvShowId,
                            seasonNumber = seasonNumber,
                            episodeNumber = episodeNumber
                        )
                )
            }
        }
    }

    private fun getImagesData() {
        viewModelScope.launch {
            val images = getTvShowImages(tvShowId)

            _uiState.update {
                it.copy(tvImages = images)
            }
        }
    }
}
