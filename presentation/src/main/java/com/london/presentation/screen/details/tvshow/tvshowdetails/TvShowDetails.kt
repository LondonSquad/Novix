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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.R
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.RatingBar
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.presentation.utils.toLocalizedNumbers
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
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height - 44) {
                            placeable.placeRelative(0, -44)
                        }
                    }
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

            OverviewSection(
                uiState = uiState,
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            )
        }

    }
}

// region CarousalSlider
@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<ImageItemEntity>
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(252.dp)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            )
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { images.size }
        )

        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            state = pagerState,
        ) { pageIndex ->
            ImageViewFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(252.dp),
                contentScale = ContentScale.FillBounds,
                model = images[pageIndex].filePath,
                placeholder = painterResource(R.drawable.img_error),
                contentDescription = "${stringResource(R.string.tv_show_image)} ${pageIndex + 1}",
            )
        }

        val dotsStates = List(images.size) { index ->
            index == pagerState.currentPage
        }

        NovixCarousalRow(
            dotsStates = dotsStates,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = NovixTheme.colors.iconBackgroundLow,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = NovixTheme.colors.stroke,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .align(Alignment.BottomCenter)

        )
    }
}
//endregion

// region Topbar
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
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier.size(40.dp)
        )

    }

}
// endregion

// region HeaderDetailsCard
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
fun ViewReviewText() {
    Text(
        text = stringResource(R.string.view_review),
        style = NovixTheme.typography.title.medium,
        color = NovixTheme.colors.primary,
    )
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
            text = "${uiState.numberOfSeasons.toLocalizedNumbers()} ${stringResource(R.string.seasons)}",
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
            imageVector = ImageVector.vectorResource(R.drawable.icon_calender),
            contentDescription = "Calender icon",
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = uiState.firstAirDate.toLocalizedNumbers(),
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
            text = uiState.voteAverage.toString().toLocalizedNumbers(),
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}
// endregion

//region OverviewSection
@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    uiState: TvShowDetailsUiState
) {
    var maxLines by rememberSaveable { mutableIntStateOf(4) }
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.overview),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title
        )

        Column {
            Text(
                text = uiState.overview,
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (isTextCollapsed)
                    stringResource(R.string.read_less) else stringResource(R.string.read_more),
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.primary,
                modifier = Modifier
                    .clickable {
                        maxLines = Int.MAX_VALUE
                        isTextCollapsed = !isTextCollapsed
                    }
            )
        }
    }
}
//endregion