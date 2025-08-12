package com.london.presentation.feature.home.trending.actor

import androidx.paging.PagingData
import com.london.domain.entity.Actor
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingActorsUiState(
    val id: Int = 0,
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val actorsFlow: Flow<PagingData<Actor>> = flow {}
)
