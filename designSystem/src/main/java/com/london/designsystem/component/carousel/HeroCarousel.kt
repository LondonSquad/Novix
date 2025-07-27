package com.london.designsystem.component.carousel

import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.carousel.m3.Carousel
import com.london.designsystem.component.carousel.m3.CarouselAlignment
import com.london.designsystem.component.carousel.m3.CarouselDefaults
import com.london.designsystem.component.carousel.m3.CarouselItemScope
import com.london.designsystem.component.carousel.m3.keylineListOf
import com.london.designsystem.component.carousel.m3.rememberCarouselState
import kotlin.math.floor
import kotlin.math.max

/**
 * A custom Material 3 Carousel with one hero item, as many small items as possible,
 * and a partially visible (cut-off) item at the end.
 *
 * @param modifier The modifier to be applied to the carousel.
 * @param itemCount The total number of items in the carousel.
 * @param heroItemSize The size of the main, focused hero item.
 * @param smallItemSize The size of the adjacent, non-focal items.
 * @param itemSpacing The spacing between items.
 * @param itemContent The composable content for each carousel item.
 */
@Composable
fun HeroCarousel(
    modifier: Modifier = Modifier,
    itemCount: Int,
    heroItemSize: Dp,
    smallItemSize: Dp,
    itemSpacing: Dp,
    itemContent: @Composable CarouselItemScope.(itemIndex: Int) -> Unit
) {
    val carouselState = rememberCarouselState { itemCount }
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

            // Add the keyline for the main hero item at the start
            add(size = heroSizePx, isAnchor = false)

            repeat(smallItemCount) { add(size = smallSizePx, isAnchor = false) }

            // Add the keyline for the partially visible "cut-off" item if there's space
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
        contentPadding = PaddingValues(end = 8.dp),
        flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(carouselState),
        content = itemContent,
        maxNonFocalVisibleItemCount = 5,
        userScrollEnabled = true
    )
}

@Composable
fun MyCarouselScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text("Hero Carousel", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            HeroCarousel(
                itemCount = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(start = 16.dp),
                heroItemSize = 158.dp,
                smallItemSize = 74.dp,
                itemSpacing = 8.dp,
            ) { itemIndex ->
                // Example item content

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