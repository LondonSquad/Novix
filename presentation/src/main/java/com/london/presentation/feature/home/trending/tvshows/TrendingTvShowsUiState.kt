package com.london.presentation.feature.home.trending.tvshows

import androidx.paging.PagingData
import com.london.domain.entity.Trending
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingTvShowsUiState(
    val tvShowsFlow: Flow<PagingData<Trending>> = flow {},
    val tvShowsGenres: List<TvShowGenre> = TvShowGenre.entries.toList(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val selectedGenreId: Int? = -1
)
