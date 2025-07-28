package com.london.designsystem.component.carousel

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.carousel.m3.Carousel
import com.london.designsystem.component.carousel.m3.CarouselAlignment
import com.london.designsystem.component.carousel.m3.CarouselDefaults
import com.london.designsystem.component.carousel.m3.CarouselItemScope
import com.london.designsystem.component.carousel.m3.CarouselState
import com.london.designsystem.component.carousel.m3.keylineListOf
import com.london.designsystem.component.carousel.m3.rememberCarouselState
import kotlin.Float
import kotlin.Int
import kotlin.Unit
import kotlin.math.floor
import kotlin.math.max
import kotlin.repeat
import kotlin.with

@Composable
fun HeroCarousel(
    carouselState: CarouselState,
    heroItemSize: Dp,
    smallItemSize: Dp,
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    flingBehavior: TargetedFlingBehavior = CarouselDefaults.multiBrowseFlingBehavior(carouselState),
    itemContent: @Composable CarouselItemScope.(itemIndex: Int) -> Unit
) {
    val density = LocalDensity.current

    val keylineList = { availableSpace: Float, itemSpacingPx: Float ->
        val heroSizePx = with(density) { heroItemSize.toPx() }
        val smallSizePx = with(density) { smallItemSize.toPx() }

        val remainingSpace = availableSpace - heroSizePx - itemSpacingPx
        val smallItemCount = max(0, floor(remainingSpace / (smallSizePx + itemSpacingPx)).toInt())

        val anchorSize = with(density) { CarouselDefaults.AnchorSize.toPx() }

        keylineListOf(
            carouselMainAxisSize = availableSpace,
            itemSpacing = itemSpacingPx,
            carouselAlignment = CarouselAlignment.Start
        ) {
            add(anchorSize, isAnchor = true)
            add(size = heroSizePx, isAnchor = false)
            repeat(smallItemCount) { add(size = smallSizePx, isAnchor = false) }

            val spaceAfterSmallItems =
                remainingSpace - (smallItemCount * (smallSizePx + itemSpacingPx))
            if (spaceAfterSmallItems > 0f) {
                add(size = smallSizePx, isAnchor = false)
            }

            add(anchorSize, isAnchor = true)
        }
    }

    Carousel(
        state = carouselState,
        orientation = Orientation.Horizontal,
        keylineList = keylineList,
        modifier = modifier,
        itemSpacing = itemSpacing,
        maxNonFocalVisibleItemCount = 5,
        flingBehavior = flingBehavior,
        contentPadding = contentPadding,
        content = itemContent
    )
}

@Composable
@Preview
fun MyCarouselScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text("Hero Carousel", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            HeroCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(start = 16.dp),
                heroItemSize = 158.dp,
                smallItemSize = 74.dp,
                itemSpacing = 8.dp,
                carouselState = rememberCarouselState { 8 }
            ) { itemIndex ->
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Item ${itemIndex + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}