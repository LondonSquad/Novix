package com.london.presentation.feature.reviews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.london.presentation.shared.RatingItem

@Composable
internal fun ReviewHeader(
    profileUrl: Any,
    authorName: String,
    authorUserName: String,
    rating: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AuthorInfo(
            profileUrl = profileUrl,
            authorName = authorName,
            authorUserName = authorUserName
        )

        RatingItem(rating = rating)
    }
}