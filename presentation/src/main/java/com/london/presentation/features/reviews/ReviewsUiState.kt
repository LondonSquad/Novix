package com.london.presentation.features.reviews

import androidx.paging.PagingData
import com.london.domain.entity.review.ReviewEntity
import com.london.presentation.features.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ReviewsUiState(
    val reviews: Flow<PagingData<ReviewEntity>> = flow {},
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)
