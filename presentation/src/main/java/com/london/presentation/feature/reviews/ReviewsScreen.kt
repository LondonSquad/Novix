package com.london.presentation.feature.reviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.ImageView
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.RatingItem
import com.london.presentation.shared.ReviewsDate
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading
import com.london.presentation.utils.reverseDateFormat

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    effect?.Listen { onNavigateBack() }

    val reviewsList = uiState.reviews.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = reviewsList.isLoading(),
        isError = reviewsList.loadState.refresh is LoadState.Error,
        onBack = onNavigateBack,
        onRetry = viewModel::onRetry
    ) {
        ReviewsScreenContent(
            uiState = uiState,
            reviewContract = viewModel,
        )
    }
}

@Composable
fun ReviewsScreenContent(
    uiState: ReviewsUiState,
    reviewContract: ReviewContract
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)

    ) {
        TopBar(
            onBackClick = reviewContract::onBackClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                )
        )

        val reviewsList = uiState.reviews.collectAsLazyPagingItems()
        if (reviewsList.itemSnapshotList.isEmpty())
            EmptyReviews(modifier = Modifier.align(CenterHorizontally))
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(reviewsList.itemCount) { index ->
                    val review = reviewsList[index]
                    if (review != null)
                        ReviewItem(
                            profileUrl = review.authorDetails.profileUrl,
                            authorName = review.authorDetails.name,
                            authorUserName = review.authorDetails.username,
                            rating = review.authorDetails.rating.toString(),
                            content = review.content,
                            date = review.createdAt.substringBefore("T")
                        )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(
    profileUrl: Any,
    authorName: String,
    authorUserName: String,
    rating: String,
    content: String,
    date: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .background(NovixTheme.colors.surface)
            .padding(12.dp)
    ) {
        ReviewHeader(
            profileUrl = profileUrl,
            authorName = authorName,
            authorUserName = authorUserName,
            rating = rating
        )

        ConditionalText(
            text = content,
            expandedState = isExpanded,
            modifier = Modifier.padding(top = 12.dp),
            minimumLineLength = 5,
        ) {
            isExpanded = !isExpanded
        }

        ReviewsDate(reverseDateFormat(date), modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
fun ReviewHeader(
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

        AuthorItem(
            profileUrl = profileUrl,
            authorName = authorName,
            authorUserName = authorUserName
        )

        RatingItem(
            rating = rating,
            modifier = Modifier
        )

    }
}

@Composable
fun AuthorItem(
    profileUrl: Any,
    authorName: String,
    authorUserName: String,
    modifier: Modifier = Modifier
) {
    Row {
        Box(
            modifier = modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = NovixTheme.colors.stroke,
                    shape = RoundedCornerShape(12.dp)
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
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(start = 8.dp),
            horizontalAlignment = CenterHorizontally,
        ) {
            Text(
                text = authorName,
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
                textAlign = TextAlign.Start,
                modifier = Modifier
            )

            Text(
                text = authorUserName,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.hint,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
fun EmptyReviews(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.no_reviews)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NovixTheme.colors.surface),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Search Icon",
            modifier = Modifier
                .size(128.dp)
                .align(CenterHorizontally),
            contentScale = ContentScale.Fit
        )
        Text(
            text = text,
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}