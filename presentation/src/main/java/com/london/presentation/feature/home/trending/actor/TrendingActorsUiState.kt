package com.london.presentation.feature.home.trending.actor

import androidx.paging.PagingData
import com.london.domain.entity.Actor
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TrendingActorsUiState(
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val isSaved: Boolean = false,
    val actorsFlow: Flow<PagingData<Actor>> = emptyFlow()
)
