package com.london.presentation.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.RatingItem
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs

private const val CARD_WIDTH_DP = 244
private const val CARD_HORIZONTAL_PADDING_DP = 8
private const val PAGE_SPACING_DP = 8

private const val ROTATION_PREVIOUS_DEGREES = 3f
private const val ROTATION_NEXT_DEGREES = -3f
private const val SCALE_CURRENT = 1f
private const val SCALE_SIDE_CARDS = 0.8f

private const val TRANSFORM_ORIGIN_X = 0.5f
private const val TRANSFORM_ORIGIN_Y = 0.9f

private const val ROTATION_OFFSET_ADJUSTMENT = 1f
private const val ROTATION_FRACTION_MULTIPLIER = 0.5f
private const val SCALE_MIN_FRACTION = 0f
private const val SCALE_MAX_FRACTION = 1f

@Composable
fun PopularSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    images: List<String>,
    cardTitle: String,
    cardRating: String,
    onSaveClick: () -> Unit,
    onCardClick: () -> Unit,
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
            if (images.size > 1) {
                while (currentCoroutineContext().isActive) {
                    delay(4000)
                    val nextPage = if (isRtl) {
                        if (pagerState.currentPage == 0) images.size - 1 else pagerState.currentPage - 1
                    } else {
                        (pagerState.currentPage + 1) % images.size
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
                    .padding(horizontal = CARD_HORIZONTAL_PADDING_DP.dp, vertical = CARD_HORIZONTAL_PADDING_DP.dp)
                    .graphicsLayer {
                        val rotationStart = if (isRtl) ROTATION_NEXT_DEGREES else ROTATION_PREVIOUS_DEGREES
                        val rotationStop = if (isRtl) ROTATION_PREVIOUS_DEGREES else ROTATION_NEXT_DEGREES

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
                    }
                    ,
            ) {

                HomeCard(
                    imageUrl = images[page],
                    onSaveClick = { onSaveClick() },
                    hasSaveIcon = pagerState.currentPage == page,
                    modifier = Modifier.clickable{ if (pagerState.currentPage == page) onCardClick() }
                )
                if (pagerState.currentPage == page)
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 6.dp, end = 8.dp)
                            .align(if (isRtl) Alignment.BottomEnd else Alignment.BottomStart),
                        horizontalAlignment = if (isRtl) Alignment.End else Alignment.Start,
                    ) {
                        Text(
                            text = cardTitle,
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
                                rating = cardRating,
                                color = NovixTheme.colors.onPrimary
                            )
                        }
                    }
            }
        }

        NovixCarousalRow(
            dotsStates = List(images.size) { index -> index == pagerState.currentPage },
        )

    }

}

@Preview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    PopularSection(
        pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 }),
        onSaveClick = {},
        onCardClick = {},
        cardTitle = "Popular",
        cardRating = "4.5",
        images = listOf(
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
        )
    )
}