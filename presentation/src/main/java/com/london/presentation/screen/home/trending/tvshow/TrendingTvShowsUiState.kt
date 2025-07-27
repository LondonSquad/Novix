package com.london.presentation.screen.home.trending.tvshow

import com.london.domain.entity.trending.Trending
import com.london.presentation.screen.base.ErrorState
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingTvShowsUiState(
    val tvShowsFlow: Flow<PagingData<Trending>> = flow {},
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val selectedGenreId: Int? = null
) 