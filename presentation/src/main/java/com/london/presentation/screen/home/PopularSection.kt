package com.london.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixCarousalRow
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
    onSaveClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
    images: List<String>
) {
    val density = LocalDensity.current
    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    val cardWidth = CARD_WIDTH_DP.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier,
            pageSpacing = PAGE_SPACING_DP.dp,
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) { page ->
            HomeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(
                        horizontal = if (page == pagerState.currentPage)
                            CARD_HORIZONTAL_PADDING_DP.dp else 0.dp
                    )
                    .graphicsLayer {
                        val pageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        rotationZ = lerp(
                            start = ROTATION_PREVIOUS_DEGREES,
                            stop = ROTATION_NEXT_DEGREES,
                            fraction = (pageOffset + ROTATION_OFFSET_ADJUSTMENT) * ROTATION_FRACTION_MULTIPLIER
                        )

                        scaleY = lerp(
                            start = SCALE_CURRENT,
                            stop = SCALE_SIDE_CARDS,
                            fraction = abs(pageOffset).coerceIn(SCALE_MIN_FRACTION, SCALE_MAX_FRACTION)
                        )

                        transformOrigin = TransformOrigin(TRANSFORM_ORIGIN_X, TRANSFORM_ORIGIN_Y)
                    },
                onCardClick = onCardClick,
                imageUrl = images[page],
                onSaveClick = { onSaveClick() },
            )
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
        images = listOf(
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
        )
    )
}