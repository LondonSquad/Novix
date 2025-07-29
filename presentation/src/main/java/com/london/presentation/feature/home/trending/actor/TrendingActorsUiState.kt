package com.london.presentation.feature.home.trending.actor

import androidx.paging.PagingData
import com.london.domain.entity.Actor
import com.london.presentation.feature.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingActorsUiState(
    val actorsFlow: Flow<PagingData<Actor>> = flow {},
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0
)
