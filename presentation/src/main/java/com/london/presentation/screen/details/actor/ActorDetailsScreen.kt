package com.london.presentation.screen.details.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ae.imageharamblur.ui.ImageViewFilter
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.presentation.R
import com.london.presentation.utils.offsetLayout
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorDetailsScreen(
    onBackClick: () -> Unit,
    onNavigateToMoviePicks: (Int) -> Unit,
    onNavigateToGallery: (Int) -> Unit,
    onNavigateToTvShowPicks: (Int) -> Unit,
    viewModel: ActorDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    ActorScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onNavigateToMoviePicks = onNavigateToMoviePicks,
        onNavigateToTvShowPicks =onNavigateToTvShowPicks,
        onNavigateToGallery = onNavigateToGallery
    )
}

@Composable
fun ActorScreenContent(
    modifier: Modifier = Modifier,
    uiState: ActorDetailsUiState,
    onNavigateToGallery: (Int) -> Unit,
    onNavigateToMoviePicks: (Int) -> Unit,
    onNavigateToTvShowPicks: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Box {
                    uiState.actorImageDetails?.let {
                        CustomBackDropImage(
                            images = it,
                        )
                    }
                }
            }

            item {
                with(uiState) {
                    if (listOf(
                            actorName,
                            actorBirthday,
                            actorPlaceOfBirth,
                            knownForDepartment
                        )
                            .all { it.isNotBlank() }
                    ) {
                        ActorInfoSection(
                            job = knownForDepartment,
                            name = actorName,
                            birthday = actorBirthday,
                            deathDay = actorDeathDay ?: "",
                            placeOfBirth = actorPlaceOfBirth,
                            modifier = Modifier.offsetLayout()
                        )
                    }
                }
            }

            item {
                if (uiState.actorBiography.isNotBlank()) {
                    Overview(
                        modifier = Modifier.padding(16.dp),
                        biography = uiState.actorBiography
                    )
                }
            }

            item {
                if (!uiState.actorImageDetails.isNullOrEmpty()) {
                    SectionHeader(
                        text = stringResource(R.string.gallery),
                        hasGetAll = true,
                        hasIcon = true,
                        modifier = Modifier
                            .padding( bottom = 12.dp)
                            .padding(horizontal = 16.dp),
                        onClick = { onNavigateToGallery(uiState.actorId) }
                    )
                    ActorGallery(images = uiState.actorImageDetails)
                }
            }


            item {
                uiState.actorMovieDetails?.cast?.takeIf { it.isNotEmpty() }?.let { movieCast ->
                    SectionHeader(
                        text = stringResource(R.string.top_movies_picks),
                        hasGetAll = true,
                        hasIcon = true,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 12.dp)
                            .padding(horizontal = 16.dp),
                        onClick = { onNavigateToMoviePicks(uiState.actorId) }
                    )
                    TopMoviesPicksList(movie = movieCast)
                }
            }

            uiState.actorTvShowDetails?.cast?.takeIf { it.isNotEmpty() }?.let { tvShows ->
                item {
                    SectionHeader(
                        text = stringResource(R.string.top_tv_shows_picks),
                        hasGetAll = true,
                        hasIcon = true,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 12.dp)
                            .padding(horizontal = 16.dp),
                        onClick = {onNavigateToTvShowPicks(uiState.actorId)}
                    )
                    TopTvShowsPicksList(tvShow = tvShows)
                }
            }

        }

        TopBar(
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ).zIndex(1f))

    }
}

@Composable
fun TopMoviesPicksList(
    movie: List<ActorMovieCastMemberEntity>
) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movie.size) { index ->
            HomeCard(
                imageUrl = movie[index].posterUrl,
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                })
        }
    }
}

@Composable
fun TopTvShowsPicksList(
    tvShow: List<ActorTvShowCastMemberEntity>
) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(tvShow.size) { index ->
            HomeCard(
                imageUrl = tvShow[index].posterUrl,
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                })
        }
    }
}

@Composable
fun ActorGallery(images: List<ImageDetails>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(88.dp)
    ) {
        itemsIndexed(images) { _, imageDetails ->
            ImageViewFilter(
                model = imageDetails.fileUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(88.dp)
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 1.dp,
                        color = NovixTheme.colors.stroke
                    )
                    .clip(RoundedCornerShape(12.dp)),
                errorContent = { ErrorImage() },
                loadingContent = { CircularLoading(modifier = Modifier.size(24.dp)) }
            )
        }
    }
}

@Composable
private fun CustomBackDropImage(
    images: List<ImageDetails>,
    modifier: Modifier = Modifier
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
        if (images.isNotEmpty()) {
            val pagerState = rememberPagerState(
                initialPage = 0, pageCount = { images.size })


            LaunchedEffect(pagerState) {
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
                    model = images[pageIndex].fileUrl,
                    contentDescription = "Actor Image ${pageIndex + 1}",
                    errorContent = { ErrorImage() },
                    loadingContent = { CircularLoading(modifier = Modifier) })
            }
        }
    }
}

@Composable
private fun ActorInfoSection(
    job: String,
    name: String,
    birthday: String,
    deathDay: String?,
    placeOfBirth: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 132.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(16.dp)
            )
            .background(NovixTheme.colors.surface),
    ) {
        Text(
            text = "${name}\n",
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
        )
        FlowRow(
            modifier = Modifier.padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = job,
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
                icon = painterResource(R.drawable.icon_location), text = placeOfBirth
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
                text = if (deathDay != "") "$birthday  -  $deathDay" else birthday
            )

        }
    }
}

@Composable
private fun Overview(
    modifier: Modifier,
    biography: String?
) {

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
            biography?.let {
                Text(
                    text = it,
                    style = NovixTheme.typography.body.small,
                    color = NovixTheme.colors.body,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = if (isTextCollapsed) stringResource(com.london.designsystem.R.string.read_less) else stringResource(
                    com.london.designsystem.R.string.read_more
                ),
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.primary,
                modifier = Modifier.clickable {
                    maxLines = if (maxLines == 4) Int.MAX_VALUE else 4
                    isTextCollapsed = !isTextCollapsed
                })
        }
    }
}


@Composable
private fun TextWithIcon(
    text: String, icon: Painter
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

