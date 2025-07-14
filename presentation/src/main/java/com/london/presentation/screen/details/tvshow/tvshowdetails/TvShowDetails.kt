package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.R
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import org.koin.androidx.compose.koinViewModel


@Composable
fun TvShowsDetailsScreen(
    viewModel: TvShowDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    TvShowsDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
fun TvShowsDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {

        TvShowScreenTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                )
                .zIndex(1f),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val images = uiState.tvImages
            if (!images.isNullOrEmpty()) {
                CustomBackDropImagePager(
                    images = images
                )
            }
            HeaderDetailsCard(
                uiState = uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-44).dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .heightIn(min = 158.dp)
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(NovixTheme.colors.surface)
            )
        }

    }
}

@Composable
fun TvShowScreenTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
            contentDescription = "back button",
            tint = NovixTheme.colors.title,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onBackClick)
                .background(
                    color = NovixTheme.colors.iconBackgroundLow,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        )

        SaveIcon(
            isSaved = false,
            onSaveClick = { },
            backgroundColor = NovixTheme.colors.iconBackgroundLow
        )

    }

}

@Composable
fun ViewReviewText() {
    Text(
        text = "View reviews",
        style = NovixTheme.typography.title.medium,
        color = NovixTheme.colors.primary,
    )
}

@Composable
fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = uiState.name,
            color = NovixTheme.colors.title,
            style = NovixTheme.typography.title.medium,
            modifier = Modifier
                .height(56.dp)
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        ) {
            GenreNames(
                uiState = uiState,
                modifier = Modifier.fillMaxWidth()
            )
            TvShowBasicDetails(
                modifier = Modifier,
                uiState = uiState
            )

            ViewReviewText()
        }
    }
}

@Composable
fun TvShowBasicDetails(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TvShowRating(uiState = uiState)

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        TvShowDate(uiState)

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.hint)
        )

        Seasons(uiState)
    }
}

@Composable
fun Seasons(uiState: TvShowDetailsUiState) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_tv),
            contentDescription = "Tv icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = "${uiState.numberOfSeasons} Seasons",
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title,
        )
    }
}

@Composable
fun TvShowDate(
    uiState: TvShowDetailsUiState
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(com.london.presentation.R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = uiState.firstAirDate,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}

@Composable
fun TvShowRating(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RatingBar(
            modifier = modifier.size(12.dp),
            rating = uiState.voteAverage.toInt(),
            onRatingChanged = {},
            maxRating = 1
        )

        Text(
            text = uiState.voteAverage.toString(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}

@Composable
fun GenreNames(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    FlowRow(
        modifier = modifier
    ) {
        uiState.tvShowGenres.forEachIndexed { index, genre ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = genre.name,
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.body,
                    modifier = if (index != uiState.tvShowGenres.lastIndex)
                        Modifier.padding(end = 8.dp) else Modifier
                )

                if (index != uiState.tvShowGenres.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(NovixTheme.colors.hint)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<ImageItemEntity>
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .height(252.dp)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            ),
        state = rememberPagerState(
            initialPage = 0,
            pageCount = { images.size }
        ),
    ) { pageIndex ->
        ImageViewFilter(
            modifier = Modifier
                .fillMaxWidth()
                .height(252.dp),
            contentScale = ContentScale.FillBounds,
            model = images[pageIndex].filePath,
            placeholder = painterResource(R.drawable.img_error),
            contentDescription = "TV Show Image ${pageIndex + 1}",
        )
    }
}