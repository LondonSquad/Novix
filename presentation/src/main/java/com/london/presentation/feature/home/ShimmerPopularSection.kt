package com.london.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.london.designsystem.utils.shimmerEffect
import kotlin.math.abs

private const val CARD_WIDTH_DP = 244
private const val CARD_HORIZONTAL_PADDING_DP = 8
private const val PAGE_SPACING_DP = 2

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
fun ShimmerPopularSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
) {
    val density = LocalDensity.current
    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    val cardWidth = CARD_WIDTH_DP.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .height(30.dp)
                .width(60.dp)
                .align(Alignment.Start)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .heightIn(CARD_WIDTH_DP.dp),
            pageSpacing = PAGE_SPACING_DP.dp,
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(

                        vertical = CARD_HORIZONTAL_PADDING_DP.dp
                    )
                    .graphicsLayer {
                        rotationZ = lerp(
                            start = ROTATION_PREVIOUS_DEGREES,
                            stop = ROTATION_NEXT_DEGREES,
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
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(),
            ) {

                repeat(3){
                    Box(modifier = Modifier
                        .height(280.dp)
                        .width(190.dp))
                }

                if (pagerState.currentPage == page)
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 6.dp, end = 8.dp)
                            .align(Alignment.BottomStart),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .height(30.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shimmerEffect()
                        )
                    }
            }
        }

    }

}