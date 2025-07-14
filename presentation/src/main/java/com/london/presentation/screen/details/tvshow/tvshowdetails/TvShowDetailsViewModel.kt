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
    savedStateHandle: SavedStateHandle
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
                it.copy(tvImages = listOf(images.backdrops[0].filePath))
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
            getTvShowDetails(tvShowId, "en-US")
        }
    }
}