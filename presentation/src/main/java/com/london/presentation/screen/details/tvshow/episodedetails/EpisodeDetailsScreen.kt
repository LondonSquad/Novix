package com.london.presentation.screen.details.tvshow.episodedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel
import com.london.designsystem.R as Res

@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    EpisodeDetailsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
fun EpisodeDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState,
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
                .zIndex(1f),
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                val images = uiState.tvImages
                if (!images.isNullOrEmpty()) {
                    CustomBackDropImagePager(
                        images = images
                    )
                }
            }

            item {
                HeaderDetailsCard(
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)

                            val yOffsetPx = with(this) { 44.dp.roundToPx() }
                            val adjustedHeight = (placeable.height - yOffsetPx).coerceAtLeast(0)

                            layout(placeable.width, adjustedHeight) {
                                placeable.placeRelative(0, -yOffsetPx)
                            }
                        }
                        .padding(start = 16.dp, end = 16.dp)
                        .heightIn(min = 158.dp)
                        .border(
                            width = 1.dp,
                            color = NovixTheme.colors.stroke,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(NovixTheme.colors.surface)
                )
            }

            item {
                OverviewSection(
                    uiState = uiState,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            }

            // Guests of honor section
            val guestStars = uiState.tvShowEpisode.guestStars
            if (!guestStars.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.guests_of_honor),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 9.dp)
                    )
                }

                items(
                    items = guestStars,
                    key = { member -> member.id }
                ) { member ->
                    ActorItem(
                        actorName = member.name,
                        characterName = member.characterName,
                        imageRes = member.profilePicture,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TvShowScreenTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(Res.drawable.arrow_left),
            contentDescription = "back button",
            tint = NovixTheme.colors.title,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onBackClick)
                .background(
                    color = NovixTheme.colors.iconBackgroundLow,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        )

        SaveIcon(
            isSaved = false,
            onSaveClick = { },
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier.size(40.dp)
        )
    }
}

// points
@Composable
fun CustomBackDropImagePager(
    modifier: Modifier = Modifier,
    images: List<ImageItemEntity>
) {
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

        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            state = pagerState,
        ) { pageIndex ->
            ImageViewFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(252.dp),
                contentScale = ContentScale.FillBounds,
                model = images[pageIndex].filePath,
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

@Composable
fun HeaderDetailsCard(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = uiState.tvShowEpisode.name,
            color = NovixTheme.colors.title,
            style = NovixTheme.typography.title.medium,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    uiState: EpisodeDetailsUiState
) {
    var maxLines by rememberSaveable { mutableIntStateOf(4) }
    var isTextCollapsed by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.overview),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title
        )

        Column {
            Text(
                text = uiState.tvShowEpisode.overview,
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (isTextCollapsed)
                    stringResource(Res.string.read_less) else stringResource(Res.string.read_more),
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

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsScreenPreview() {
    NovixTheme {
        EpisodeDetailsScreenContent(
            uiState = EpisodeDetailsUiState(
                tvImages = listOf(
                    ImageItemEntity(
                        filePath = "https://tse3.mm.bing.net/th/id/OIP.U_VJuupQohwnzXcKMztqWgHaEo?rs=1&pid=ImgDetMain&o=7&rm=3",
                        aspectRatio = 1.78,
                        height = 720,
                        width = 1280,
                        iso6391 = "en",
                        voteAverage = 8.5,
                        voteCount = 150
                    ),
                    ImageItemEntity(
                        filePath = "https://image.tmdb.org/t/p/w500/sample2.jpg",
                        aspectRatio = 1.78,
                        height = 720,
                        width = 1280,
                        iso6391 = "en",
                        voteAverage = 7.8,
                        voteCount = 120
                    )
                ),
                tvShowEpisode = TvShowEpisodeByIdEntity(
                    id = 1,
                    name = "The One Where Monica Gets a Roommate",
                    overview = "Monica and the gang introduce Rachel to the real world after she leaves her fiancé at the altar. This is a longer overview to test the read more/less functionality in the UI. It should show how the text expands and collapses when the user taps the read more button.",
                    airDate = "1994-09-22",
                    episodeNumber = 1,
                    seasonNumber = 1,
                    episodeTypes = "Standard",
                    tvShowId = 1399,
                    stillPath = "/sample-still.jpg",
                    voteAverage = 8.5,
                    voteCount = 1250,
                    guestStars = listOf(
                        Actor(
                            id = 1,
                            name = "Jane Doe",
                            characterName = "Guest Character 1",
                            profilePicture = "https://image.tmdb.org/t/p/w185/sample-profile1.jpg"
                        ),
                        Actor(
                            id = 2,
                            name = "John Smith",
                            characterName = "Guest Character 2",
                            profilePicture = "https://image.tmdb.org/t/p/w185/sample-profile2.jpg"
                        )
                    )
                )
            ),
            onBackClick = {}
        )
    }
}
