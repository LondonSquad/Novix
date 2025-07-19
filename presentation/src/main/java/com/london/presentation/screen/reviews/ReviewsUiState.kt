package com.london.presentation.screen.reviews

import androidx.paging.PagingData
import com.london.domain.entity.review.ReviewEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ReviewsUiState(
    val reviews: Flow<PagingData<ReviewEntity>> = flow {},
)