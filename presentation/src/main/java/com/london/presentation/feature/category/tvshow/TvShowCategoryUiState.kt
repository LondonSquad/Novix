package com.london.presentation.feature.category.tvshow

import androidx.paging.PagingData
import com.london.domain.entity.tvshow.TvShow
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.TvShowGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TvShowCategoryUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val genre: TvShowGenreUi = TvShowGenreUi.All,
    val tvShowFlow: Flow<PagingData<TvShow>> = flow {}
)
