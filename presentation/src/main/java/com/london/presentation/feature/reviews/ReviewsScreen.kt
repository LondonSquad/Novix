package com.london.presentation.feature.reviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.review.Review
import com.london.presentation.R
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.ImageView
import com.london.presentation.shared.RatingItem
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading
import com.london.presentation.utils.reverseDateFormat
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    effect?.Listen {
        when (it) {
            is ReviewEffect.NavigateBack -> onNavigateBack()
        }
    }

    val reviewsList = uiState.reviews.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = reviewsList.isLoading(),
        isError = reviewsList.loadState.refresh is LoadState.Error,
        onBack = viewModel::onBackClicked,
        onRetry = viewModel::onRetry
    ) {
        Content(
            reviewsList = reviewsList,
            reviewContract = viewModel,
        )
    }
}

@Composable
private fun Content(
    reviewsList: LazyPagingItems<Review>,
    reviewContract: ReviewContract
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        ReviewsTopBar(
            onBackClick = reviewContract::onBackClicked
        )

        ReviewsContent(
            reviewsList = reviewsList,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ReviewsContent(
    reviewsList: LazyPagingItems<Review>,
    modifier: Modifier = Modifier
) {
    if (reviewsList.itemSnapshotList.isEmpty()) {
        EmptyReviewsState(
            modifier = modifier.fillMaxSize()
        )
    } else {
        ReviewsList(
            reviewsList = reviewsList,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ReviewsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopBar(
        title = stringResource(R.string.reviews),
        onBackClick = onBackClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReviewsDimens.HorizontalPadding)
            .padding(top = ReviewsDimens.TopBarTopPadding)
    )
}

@Composable
private fun EmptyReviewsState(
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

@Composable
private fun ReviewsList(
    reviewsList: LazyPagingItems<Review>,
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

@Composable
private fun ReviewItem(
    review: Review,
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

@Composable
private fun ReviewHeader(
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

@Composable
private fun AuthorInfo(
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

@Composable
private fun ReviewsDate(
    date: String,
    modifier: Modifier = Modifier,
) {
    if (date.isEmpty() || date.isBlank()) return
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(com.london.designsystem.R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(11.dp)
        )

        Text(
            text = date.toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body
        )
    }
}