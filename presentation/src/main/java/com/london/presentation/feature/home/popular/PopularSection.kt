package com.london.presentation.feature.home.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.MediaType.Companion.isMovie
import com.london.presentation.R
import com.london.presentation.feature.home.popular.PopularSection.CARD_HORIZONTAL_PADDING_DP
import com.london.presentation.feature.home.popular.PopularSection.CARD_WIDTH_DP
import com.london.presentation.feature.home.popular.PopularSection.PAGE_SPACING_DP
import com.london.presentation.feature.home.popular.PopularSection.ROTATION_FRACTION_MULTIPLIER
import com.london.presentation.feature.home.popular.PopularSection.ROTATION_NEXT_DEGREES
import com.london.presentation.feature.home.popular.PopularSection.ROTATION_OFFSET_ADJUSTMENT
import com.london.presentation.feature.home.popular.PopularSection.ROTATION_PREVIOUS_DEGREES
import com.london.presentation.feature.home.popular.PopularSection.SCALE_CURRENT
import com.london.presentation.feature.home.popular.PopularSection.SCALE_MAX_FRACTION
import com.london.presentation.feature.home.popular.PopularSection.SCALE_MIN_FRACTION
import com.london.presentation.feature.home.popular.PopularSection.SCALE_SIDE_CARDS
import com.london.presentation.feature.home.popular.PopularSection.TRANSFORM_ORIGIN_X
import com.london.presentation.feature.home.popular.PopularSection.TRANSFORM_ORIGIN_Y
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.RatingItem
import com.london.presentation.utils.toLocalizedNumbers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs

@Composable
fun PopularSection(
    pagerState: PagerState,
    uiMediaList: List<PopularUiMedia>,
    onCardClick: (Int, MediaType) -> Unit,
    onManageBookmarkClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Content(
        modifier = modifier,
        pagerState = pagerState,
        uiMediaList = uiMediaList,
        onCardClick = onCardClick,
        onManageBookmarkClicked = onManageBookmarkClicked
    )
}

@Composable
private fun Content(
    pagerState: PagerState,
    uiMediaList: List<PopularUiMedia>,
    onManageBookmarkClicked: (Int) -> Unit,
    onCardClick: (Int, MediaType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    val cardWidth = CARD_WIDTH_DP.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = stringResource(R.string.popular),
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(Alignment.Start)
        )

        LaunchedEffect(Unit) {
            if (uiMediaList.size > 1) {
                while (currentCoroutineContext().isActive) {
                    delay(4000)
                    val nextPage = if (isRtl) {
                        if (pagerState.currentPage == 0) uiMediaList.size - 1 else pagerState.currentPage - 1
                    } else {
                        (pagerState.currentPage + 1) % uiMediaList.size
                    }
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .heightIn(CARD_WIDTH_DP.dp),
            pageSpacing = PAGE_SPACING_DP.dp,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            reverseLayout = isRtl
        ) { page ->
            val pageOffset = if (isRtl) {
                (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction
            } else {
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(
                        horizontal = CARD_HORIZONTAL_PADDING_DP.dp,
                        vertical = CARD_HORIZONTAL_PADDING_DP.dp
                    )
                    .graphicsLayer {
                        val rotationStart =
                            if (isRtl) ROTATION_NEXT_DEGREES else ROTATION_PREVIOUS_DEGREES
                        val rotationStop =
                            if (isRtl) ROTATION_PREVIOUS_DEGREES else ROTATION_NEXT_DEGREES

                        rotationZ = lerp(
                            start = rotationStart,
                            stop = rotationStop,
                            fraction = (pageOffset + ROTATION_OFFSET_ADJUSTMENT) * ROTATION_FRACTION_MULTIPLIER
                        )

                        scaleY = lerp(
                            start = SCALE_CURRENT,
                            stop = SCALE_SIDE_CARDS,
                            fraction = abs(pageOffset).coerceIn(
                                SCALE_MIN_FRACTION,
                                SCALE_MAX_FRACTION
                            )
                        )

                        transformOrigin = TransformOrigin(TRANSFORM_ORIGIN_X, TRANSFORM_ORIGIN_Y)
                    },
            ) {

                HomeCard(
                    imageUrl = uiMediaList[page].posterUrl,
                    onSaveClick = { onManageBookmarkClicked(uiMediaList[page].id) },
                    hasSaveIcon = pagerState.currentPage == page && uiMediaList[page].mediaType.isMovie(),
                    modifier = Modifier.clickable {
                        if (pagerState.currentPage == page) onCardClick(
                            uiMediaList[page].id,
                            uiMediaList[page].mediaType
                        )
                    }
                )

                if (pagerState.currentPage == page)
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 6.dp, end = 8.dp)
                            .align(if (isRtl) Alignment.BottomEnd else Alignment.BottomStart),
                        horizontalAlignment = if (isRtl) Alignment.End else Alignment.Start,
                    ) {
                        Text(
                            text = uiMediaList[page].name,
                            style = NovixTheme.typography.label.medium,
                            color = NovixTheme.colors.onPrimary,
                            maxLines = 2,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )

                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (isRtl) Arrangement.End else Arrangement.Start
                        ) {
                            RatingItem(
                                rating = uiMediaList[page].rating.toLocalizedNumbers(),
                                color = NovixTheme.colors.onPrimary
                            )
                        }
                    }
            }
        }

        NovixCarousalRow(
            modifier = Modifier.height(8.dp),
            dotsStates = List(uiMediaList.size) { index -> index == pagerState.currentPage },
        )

    }

}

private object PopularSection {
    const val CARD_WIDTH_DP = 244
    const val CARD_HORIZONTAL_PADDING_DP = 8
    const val PAGE_SPACING_DP = 8

    const val ROTATION_PREVIOUS_DEGREES = 3f
    const val ROTATION_NEXT_DEGREES = -3f
    const val SCALE_CURRENT = 1f
    const val SCALE_SIDE_CARDS = 0.8f

    const val TRANSFORM_ORIGIN_X = 0.5f
    const val TRANSFORM_ORIGIN_Y = 0.9f

    const val ROTATION_FRACTION_MULTIPLIER = 0.5f
    const val SCALE_MIN_FRACTION = 0f
    const val SCALE_MAX_FRACTION = 1f
    const val ROTATION_OFFSET_ADJUSTMENT = 1f
}

@Preview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    PopularSection(
        pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 }),
        onCardClick = { id, mediaType -> },
        uiMediaList = listOf(
            PopularUiMedia(
                name = "Popular",
                rating = "4.5",
                id = 0,
                posterUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                mediaType = MediaType.Movie
            ),
            PopularUiMedia(
                name = "Popular",
                rating = "4.5",
                id = 1,
                posterUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                mediaType = MediaType.Movie
            ),
            PopularUiMedia(
                name = "Popular",
                rating = "4.5",
                id = 2,
                posterUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                mediaType = MediaType.Movie
            ),
            PopularUiMedia(
                name = "Popular",
                rating = "4.5",
                id = 3,
                posterUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                mediaType = MediaType.Movie
            ),
            PopularUiMedia(
                name = "Popular",
                rating = "4.5",
                id = 4,
                posterUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                mediaType = MediaType.Movie
            ),
        ),
        onManageBookmarkClicked = { },
    )
}