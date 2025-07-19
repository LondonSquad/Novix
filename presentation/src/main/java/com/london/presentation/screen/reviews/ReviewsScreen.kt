package com.london.presentation.screen.reviews

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.zIndex
import androidx.paging.compose.collectAsLazyPagingItems
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.ButtonIcon
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.composables.ConditionalText
import com.london.presentation.composables.RatingItem
import com.london.presentation.composables.ReviewsDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    ReviewsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@Composable
fun ReviewsScreenContent(
    uiState: ReviewsUiState,
    onBackClick: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val shouldShowBackground by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemScrollOffset > 40f ||
                    lazyListState.firstVisibleItemIndex > 0
        }
    }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (shouldShowBackground) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "background_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)

    ) {

        val reviewsList = uiState.reviews.collectAsLazyPagingItems()
        if (reviewsList.itemSnapshotList.isEmpty())
            EmptyReviews(
                modifier = Modifier.fillMaxSize()
            )
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(16.dp)
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
                                64.dp
                    )
                    .background(
                        NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
                    )
                    .zIndex(0.5f)
            )

            ReviewTopBar(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp
                    )
                    .align(Alignment.TopCenter),
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
fun ReviewTopBar(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1.0f)
            .background(NovixTheme.colors.surface)
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            ),
        contentAlignment = Alignment.Center
    ) {

        ButtonIcon(
            onClick = onBackClick,
            iconRes = com.london.designsystem.R.drawable.arrow_left,
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart),
        )
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

        ReviewsDate(date, modifier = Modifier.padding(top = 12.dp))
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AuthorItem(
            profileUrl = profileUrl,
            authorName = authorName,
            authorUserName = authorUserName
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            RatingItem(
                rating = rating,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun AuthorItem(
    profileUrl: Any,
    authorName: String,
    authorUserName: String,
    modifier: Modifier = Modifier
) {
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
        ImageViewFilter(
            model = profileUrl,
            contentDescription = stringResource(R.string.author_profile),
            modifier = Modifier,
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
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = authorUserName,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.hint,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
    }
}

@Composable
fun EmptyReviews(
    modifier: Modifier = Modifier,
    text: String = "there is no review"
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