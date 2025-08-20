package com.london.presentation.feature.reviews

import androidx.paging.PagingData
import com.london.domain.entity.review.Review
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ReviewsUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val reviews: Flow<PagingData<Review>> = flow {},
)
