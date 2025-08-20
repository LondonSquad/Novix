package com.london.presentation.feature.home.trending.tvshow

import androidx.paging.PagingData
import com.london.domain.entity.shared.Trending
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.TvShowGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TrendingTvShowsUiState(
    val isLoading: Boolean = false,
    val selectedGenreId: Int? = null,
    val errorState: ErrorState? = null,
    val tvShowsFlow: Flow<PagingData<Trending>> = emptyFlow(),
    val tvShowsGenres: List<TvShowGenreUi> = TvShowGenreUi.getList(),
    val selectedGenre: TvShowGenreUi = TvShowGenreUi.All
)
