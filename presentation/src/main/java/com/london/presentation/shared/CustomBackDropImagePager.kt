package com.london.presentation.shared

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.ImageView
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.UnSuitableEye
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<String>,
    isVisibleDots: Boolean
) {
    val validImages = images.filter { it.isNotBlank() }

    if (validImages.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(252.dp)
                .background(NovixTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            ImageVerticalGradient()
            ErrorImage()
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
                pageCount = { validImages.size }
            )

            LaunchedEffect(Unit) {
                if (validImages.size > 1) {
                    while (currentCoroutineContext().isActive) {
                        delay(4000)
                        val nextPage = (pagerState.currentPage + 1) % validImages.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }

            HorizontalPager(
                modifier = Modifier.align(Alignment.Center),
                state = pagerState,
            ) { pageIndex ->
                ImageView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp),
                    contentScale = ContentScale.FillBounds,
                    model = validImages[pageIndex],
                    contentDescription = "${stringResource(R.string.tv_show_image)} ${pageIndex + 1}",
                    errorContent = { ErrorImage() },
                    loadingContent = { CircularLoading(modifier = Modifier) },
                    moderatedContent = { UnSuitableEye() }
                )
            }

            if (validImages.size > 1 && isVisibleDots) {
                NovixCarousalRow(
                    dotsStates = List(validImages.size) { index -> index == pagerState.currentPage },
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
            ImageVerticalGradient()
        }
    }
}

@Composable
private fun ImageVerticalGradient() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(252.dp)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NovixTheme.colors.blackLinearGradient,
                        NovixTheme.colors.whiteLinearGradient
                    )
                )
            )
    )
}
