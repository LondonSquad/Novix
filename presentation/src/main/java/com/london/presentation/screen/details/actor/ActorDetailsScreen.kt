package com.london.presentation.screen.details.actor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
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

        TopBar(
            onBackClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp)
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
            item {
                Overview(
                    modifier = Modifier.padding(16.dp)
                )
            }
            item{
                SectionHeader(
                    text = stringResource(R.string.gallery),
                    hasGetAll = true,
                    hasIcon = true,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 12.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                ActorGallery()
            }
            item{
                SectionHeader(
                    text = stringResource(R.string.top_movies_picks),
                    hasGetAll = true,
                    hasIcon = true,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 12.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                TopMoviesPicksList()
            }
            item{
                SectionHeader(
                    text = stringResource(R.string.top_tv_shows_picks),
                    hasGetAll = true,
                    hasIcon = true,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 12.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                TopTvShowsPicksList()
            }
        }
    }
}

@Composable
fun TopMoviesPicksList() {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(10) {
            HomeCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/ajNaPmXVVMJFg9GWmu6MJzTaXdV.jpg",
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                }
            )
        }
    }
}

@Composable
fun TopTvShowsPicksList() {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(10) {
            HomeCard(
                imageUrl = "https://image.tmdb.org/t/p/w500/ajNaPmXVVMJFg9GWmu6MJzTaXdV.jpg",
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                }
            )
        }
    }
}

@Composable
fun ActorGallery() {
    val images = listOf(
        R.drawable.actor_image,
        R.drawable.actor_image,
        R.drawable.actor_image,
        R.drawable.actor_image,
        R.drawable.actor_image,
        R.drawable.actor_image,
    )

    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 88.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(88.dp)
    ) {
        itemsIndexed(images) { _, imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(88.dp)
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 1.dp,
                        color = NovixTheme.colors.stroke
                    )
                    .clip(RoundedCornerShape(12.dp))
            )
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
    }
}

@Composable
private fun ActorInfoSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 132.dp)
            .padding(16.dp)
            .background(NovixTheme.colors.surface)
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp),
        ) {
            Text(
                text = "Tom Hanks\n",
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
            )
            FlowRow(
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Acting",
                    style = NovixTheme.typography.label.small,
                    color = NovixTheme.colors.body,
                )
                Icon(
                    painter = painterResource(R.drawable.image_dot),
                    contentDescription = stringResource(R.string.imagr_dot),
                    tint = NovixTheme.colors.body,
                    modifier = Modifier
                        .size(3.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
                TextWithIcon(
                    icon = painterResource(R.drawable.icon_location),
                    text = "Santa Cruz del Norte, Cuba"
                )
                Icon(
                    painter = painterResource(R.drawable.image_dot),
                    contentDescription = stringResource(R.string.imagr_dot),
                    tint = NovixTheme.colors.body,
                    modifier = Modifier
                        .size(3.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
                TextWithIcon(
                    icon = painterResource(R.drawable.birthday_cake),
                    text = "1988-04-30  -  2012-30-03"
                )
            }
        }
    }
}

@Composable
private fun Overview(
    modifier: Modifier
){

    var maxLines by rememberSaveable { mutableIntStateOf(4) }
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
    Text(
        text = stringResource(R.string.biography),
        style = NovixTheme.typography.title.medium,
        color = NovixTheme.colors.title
    )

    Column {
        Text(
            text = "Matthew Paige Damon is an American actor, film producer, and screenwriter. He was ranked among Forbes most bankable stars in 2007 and, in 2010, was one of the highest-grossing ",
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (isTextCollapsed)
                stringResource(com.london.designsystem.R.string.read_less) else stringResource(com.london.designsystem.R.string.read_more),
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.primary,
            modifier = Modifier
                .clickable {
                    maxLines = if (maxLines == 4) Int.MAX_VALUE else 4
                    isTextCollapsed = !isTextCollapsed
                }
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
        )
    }
}

@Preview
@Composable
fun ActorDetailsScreenPreview() {
    ActorDetailsScreen()
}

