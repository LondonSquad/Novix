package com.london.presentation.screen.details.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.details.tvshow.tvshowdetails.TvShowScreenTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToGallery: (Int) -> Unit = { },
    onNavigateToMoviePicks: (Int) -> Unit = { },
    onNavigateToTvShowPicks: (Int) -> Unit = { },
    viewModel: ActorDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    ActorScreenContent(
//        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
fun ActorScreenContent(
    modifier: Modifier = Modifier,
//    uiState: ActorDetailsUiState,
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
                .zIndex(1f), onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
//                val images = uiState.actorImageDetails
                CustomBackDropImage(
                    images = listOf(
                        painterResource(R.drawable.actor_image),
                        painterResource(R.drawable.actor_image),
                        painterResource(R.drawable.actor_image),
                        painterResource(R.drawable.actor_image),
                        painterResource(R.drawable.actor_image),
                        painterResource(R.drawable.actor_image),
                    )
                )
            }

            item {
                ActorInfoSection()
            }
        }
    }
}

@Composable
private fun CustomBackDropImage(
    modifier: Modifier = Modifier, images: List<Painter>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(252.dp)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 12.dp, bottomEnd = 12.dp
                )
            )
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0, pageCount = { images.size })

        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            state = pagerState,
        ) { pageIndex ->
            ImageViewFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(252.dp),
                contentScale = ContentScale.FillBounds,
                model = images,
//                model = images[pageIndex].filePath,
                contentDescription = "TV Show Image ${pageIndex + 1}",
                errorContent = { ErrorImage() },
                loadingContent = { CircularLoading(modifier = Modifier) })
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
                    color = NovixTheme.colors.iconBackgroundLow, shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ActorInfoSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(328.dp)
            .heightIn(min = 132.dp)
            .padding(12.dp)
            .background(NovixTheme.colors.surface)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )  {
        Text(
            text = "Tom Hanks\n",
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = "Acting",
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.body,
                lineHeight = 18.sp,
                modifier = Modifier.alignByBaseline()
            )
            Icon(
                painter = painterResource(R.drawable.image_dot),
                contentDescription = stringResource(R.string.imagr_dot),
                tint = NovixTheme.colors.body,
            )
            TextWithIcon(
                icon = painterResource(R.drawable.icon_location),
                text = "Santa Cruz del Norte, Cuba"
            )
        }
    }
}

@Composable
private fun TextWithIcon(
    text: String,
    icon: Painter
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = stringResource(R.string.imagr_dot),
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body,
            modifier = Modifier.alignByBaseline(),
            lineHeight = 18.sp
        )
    }
}

@Preview
@Composable
fun ActorDetailsScreenPreview() {
    ActorDetailsScreen()
}

