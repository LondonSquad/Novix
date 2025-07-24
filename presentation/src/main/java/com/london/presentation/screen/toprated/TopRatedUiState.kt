package com.london.presentation.screen.toprated

import androidx.paging.PagingData
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries
import kotlinx.coroutines.flow.Flow

data class TopRatedUiState(
    val genre: List<GenreUiState>? = null,
    val media: MediaUiState = MediaUiState.Empty,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class MediaUiState {
    data object Empty : MediaUiState()
    data class Combined(
        val movies: Flow<PagingData<TopRatedMovie>>,
        val tvSeries: Flow<PagingData<TopRatedTvSeries>>
    ) : MediaUiState()
}

data class GenreUiState(
    val genreId: Int = 0,
    val genreName: String = "",
    val isSelected: Boolean = false,
)

