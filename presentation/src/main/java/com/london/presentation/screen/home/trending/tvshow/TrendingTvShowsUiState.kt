package com.london.presentation.screen.home.trending.tvshow

import com.london.domain.entity.trending.Trending
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Genre
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingTvShowsUiState(
    val trendingTvShows: Flow<PagingData<Trending>> = flow {},
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null
) 