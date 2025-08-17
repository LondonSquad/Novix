package com.london.presentation.feature.reviews.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.reviews.ReviewsDimens

@Composable
internal fun EmptyReviewsState(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.no_reviews)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovixTheme.colors.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier.size(ReviewsDimens.EmptyStateImageSize),
                contentScale = ContentScale.Fit
            )

            Text(
                text = text,
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ReviewsDimens.ContentPadding)
            )
        }
    }
}