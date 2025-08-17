package com.london.presentation.feature.reviews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.london.domain.entity.review.ReviewEntity
import com.london.presentation.feature.reviews.ReviewsDimens
import com.london.presentation.utils.reverseDateFormat

@Composable
internal fun ReviewsList(
    reviewsList: LazyPagingItems<ReviewEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ReviewsDimens.ItemSpacing),
        contentPadding = PaddingValues(ReviewsDimens.ContentPadding),
    ) {
        items(reviewsList.itemCount) { index ->
            reviewsList[index]?.let { review ->
                ReviewItem(
                    review = review,
                    date = review.createdAt.substringBefore("T")
                        .let { reverseDateFormat(it) }
                )
            }
        }
    }
}