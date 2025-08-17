package com.london.presentation.feature.reviews.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.reviews.ReviewsDimens
import com.london.presentation.shared.ImageView

@Composable
internal fun AuthorInfo(
    profileUrl: Any,
    authorName: String,
    authorUserName: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AuthorProfileImage(profileUrl = profileUrl)
        AuthorDetails(
            authorName = authorName,
            authorUserName = authorUserName,
            modifier = Modifier.padding(start = ReviewsDimens.AuthorInfoStartPadding)
        )
    }
}

@Composable
private fun AuthorProfileImage(
    profileUrl: Any,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(ReviewsDimens.ProfileImageSize)
            .clip(RoundedCornerShape(ReviewsDimens.CornerRadius))
            .border(
                width = ReviewsDimens.BorderWidth,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(ReviewsDimens.CornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        ImageView(
            model = profileUrl,
            contentDescription = stringResource(R.string.author_profile),
            contentScale = ContentScale.Crop,
            loadingContent = { CircularLoading() },
            errorContent = { ErrorImage() },
        )
    }
}

@Composable
private fun AuthorDetails(
    authorName: String,
    authorUserName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = authorName,
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            textAlign = TextAlign.Start
        )

        Text(
            text = authorUserName,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.hint,
            textAlign = TextAlign.Start
        )
    }
}