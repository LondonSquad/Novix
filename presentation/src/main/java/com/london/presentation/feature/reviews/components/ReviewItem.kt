package com.london.presentation.feature.reviews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.review.ReviewEntity
import com.london.presentation.feature.reviews.ReviewsDimens
import com.london.presentation.shared.ConditionalText

@Composable
internal fun ReviewItem(
    review: ReviewEntity,
    date: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(ReviewsDimens.CornerRadius))
            .border(
                width = ReviewsDimens.BorderWidth,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(ReviewsDimens.CornerRadius)
            )
            .background(NovixTheme.colors.surface)
            .padding(ReviewsDimens.ItemPadding)
    ) {
        ReviewHeader(
            profileUrl = review.authorDetails.profileUrl,
            authorName = review.authorDetails.name,
            authorUserName = review.authorDetails.username,
            rating = review.authorDetails.rating.toString()
        )

        ConditionalText(
            text = review.content,
            expandedState = isExpanded,
            modifier = Modifier.padding(top = ReviewsDimens.ContentTopPadding),
            minimumLineLength = 5,
        ) {
            isExpanded = !isExpanded
        }

        ReviewsDate(
            date = date,
            modifier = Modifier.padding(top = ReviewsDimens.ContentTopPadding)
        )
    }
}