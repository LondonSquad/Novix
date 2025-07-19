package com.london.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import kotlinx.coroutines.delay

@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<String>
) {
    if (images.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(252.dp)
                .background(NovixTheme.colors.surface)
        ) {
            NovixCarousalRow(
                dotsStates = listOf(false),
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
    } else {
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

            LaunchedEffect(Unit) {
                if (images.size > 1) {
                    while (true) {
                        delay(4000)
                        val nextPage = (pagerState.currentPage + 1) % images.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }

            HorizontalPager(
                modifier = Modifier.align(Alignment.Center),
                state = pagerState,
            ) { pageIndex ->
                ImageViewFilter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp),
                    contentScale = ContentScale.FillBounds,
                    model = images[pageIndex],
                    contentDescription = "TV Show Image ${pageIndex + 1}",
                    errorContent = { ErrorImage() },
                    loadingContent = { CircularLoading(modifier = Modifier) },
                    moderatedContent = { UnSuitableEye() }
                )
            }

            NovixCarousalRow(
                dotsStates = List(images.size) { index -> index == pagerState.currentPage },
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
}