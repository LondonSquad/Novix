package com.london.presentation.screen.reviews

import androidx.paging.PagingData
import com.london.domain.entity.review.ReviewEntity
import com.london.presentation.screen.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ReviewsUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val reviews: Flow<PagingData<ReviewEntity>> = flow {},
)