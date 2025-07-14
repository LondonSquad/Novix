package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.theme.NovixTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun TvShowsDetailsScreen(
    viewModel: TvShowDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
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
        }
    }
}

@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<String>
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .height(252.dp),
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
            model = images[pageIndex],
            placeholder = painterResource(com.london.designsystem.R.drawable.img_error),
            contentDescription = "TV Show Image ${pageIndex + 1}",
        )
    }
}