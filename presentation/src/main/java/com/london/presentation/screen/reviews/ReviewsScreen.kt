package com.london.presentation.screen.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.composables.RatingItem
import com.london.presentation.composables.ReadMoreText
import com.london.presentation.composables.TvShowDate
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
        modifier = Modifier
    )
}

@Composable
fun ReviewsScreenContent(
    uiState: ReviewsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)

    ) {
        val reviewsList = uiState.reviews.collectAsLazyPagingItems()

        TopBar(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                ),
            title = "Reviews",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
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
                        date = review.createdAt
                    )
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(NovixTheme.colors.surface)
            .padding(12.dp)
    ) {
        ReviewHeader(
            profileUrl = profileUrl,
            authorName = authorName,
            authorUserName = authorUserName,
            rating = rating
        )

        ReadMoreText(
            content = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            maxLines = 5
        )

        TvShowDate(date, modifier = Modifier.padding(top = 12.dp))
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
                voteAverage = rating,
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
    ImageViewFilter(
        model = profileUrl,
        contentDescription = "",
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = NovixTheme.colors.stroke
            ),
        contentScale = ContentScale.Crop,
        loadingContent = { CircularLoading() },
        errorContent = { ErrorImage() },
    )

    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
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