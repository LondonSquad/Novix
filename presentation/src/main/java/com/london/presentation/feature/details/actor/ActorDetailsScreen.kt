package com.london.presentation.feature.details.actor

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.Icon
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.imageharamblur.ui.ImageViewFilter
import com.london.presentation.R
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.utils.Listen
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.toLocalizedNumbers
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActorDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMoviePicks: (Int) -> Unit,
    onNavigateToGallery: (Int) -> Unit,
    onNavigateToTvShowPicks: (Int) -> Unit,
    onNavigateToMovieScreen: (Int) -> Unit,
    onNavigateToTvShowScreen: (Int) -> Unit,
    viewModel: ActorDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is ActorEffectUiState.NavigationBack -> onNavigateBack()
            is ActorEffectUiState.NavigateToGallery -> onNavigateToGallery(currentEffect.actorId)
            is ActorEffectUiState.NavigateToMovieScreen -> {
                onNavigateToMovieScreen(currentEffect.movieId)
            }

            is ActorEffectUiState.NavigateToTvShowPicks -> onNavigateToTvShowPicks(uiState.actorId)
            is ActorEffectUiState.NavigateToTvShowScreen -> onNavigateToTvShowScreen(currentEffect.tvShowId)
            is ActorEffectUiState.NavigateToMoviePicks -> onNavigateToMoviePicks(uiState.actorId)
        }
    }

    ActorScreenContent(
        uiState = uiState,
        actorDetailsContract = viewModel
    )
}

@Composable
fun ActorScreenContent(
    modifier: Modifier = Modifier,
    uiState: ActorDetailsUiState,
    actorDetailsContract: ActorDetailsContract,
) {
    val lazyState = rememberLazyListState()

    val shouldShowBackground by remember {
        derivedStateOf {
            lazyState.firstVisibleItemScrollOffset > 40f ||
                    lazyState.firstVisibleItemIndex > 0
        }
    }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (shouldShowBackground) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "background_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 16.dp
            ),
            state = lazyState
        ) {
            item {
                ActorImagePager(images = uiState.actorImageDetails.orEmpty())
            }

            item {
                ActorInfoSectionItem(uiState = uiState)
            }

            item {
                BiographySection(uiState = uiState)
            }

            item {
                GallerySection(
                    images = uiState.actorImageDetails,
                    onGalleryClick = { actorDetailsContract.onGalleryClick(uiState.actorId) }
                )
            }

            item {
                MoviesSection(
                    movies = uiState.actorMovieDetails?.cast,
                    onMoviePicksClick = { actorDetailsContract.onMoviePicksClick(uiState.actorId) },
                    onMovieScreenClick = actorDetailsContract::onMovieScreenClick
                )
            }

            item {
                TvShowsSection(
                    tvShows = uiState.actorTvShowDetails?.cast,
                    onTvShowPicksClick = { actorDetailsContract.onTvShowPicksClick(uiState.actorId) },
                    onTvShowScreenClick = actorDetailsContract::onTvShowScreenClick
                )
            }
        }

        TopBar(
            onBackClick = actorDetailsContract::onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
                )
                .padding(
                    start = 16.dp,
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                )
                .zIndex(1f)
        )
    }
}

@Composable
private fun ActorImagePager(images: List<ImageDetails>) {
    CustomBackDropImagePager(
        images = images.map { it.fileUrl },
        isVisibleDots = false
    )
}

@Composable
private fun ActorInfoSectionItem(uiState: ActorDetailsUiState) {
    with(uiState) {
        if (listOf(
                actorName,
                actorBirthday,
                actorPlaceOfBirth,
                knownForDepartment
            ).all { it.isNotBlank() }
        ) {
            ActorInfoSection(
                job = knownForDepartment,
                name = actorName,
                birthday = actorBirthday,
                deathDay = actorDeathDay ?: "",
                placeOfBirth = actorPlaceOfBirth,
                modifier = Modifier.offsetLayout()
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BiographySection(uiState: ActorDetailsUiState) {
    if (uiState.actorBiography.isNotBlank()) {
        Text(
            text = stringResource(R.string.biography),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 4.dp,
            )
        )
        var isExpanded by remember { mutableStateOf(false) }

        ConditionalText(
            text = uiState.actorBiography,
            expandedState = isExpanded,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            isExpanded = !isExpanded
        }
    } else {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GallerySection(
    images: List<ImageDetails>?,
    onGalleryClick: () -> Unit
) {
    if (!images.isNullOrEmpty()) {
        SectionHeader(
            text = stringResource(R.string.gallery),
            hasGetAll = true,
            hasIcon = true,
            modifier = Modifier
                .padding(bottom = 12.dp, top = 16.dp)
                .padding(horizontal = 16.dp),
            onClick = onGalleryClick
        )
        ActorGallery(images = images)
    }
}

@Composable
private fun MoviesSection(
    movies: List<ActorMovieCastMemberEntity>?,
    onMoviePicksClick: () -> Unit,
    onMovieScreenClick: (Int) -> Unit
) {
    movies?.takeIf { it.isNotEmpty() }?.let { movieCast ->
        SectionHeader(
            text = stringResource(R.string.top_movies_picks),
            hasGetAll = true,
            hasIcon = true,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 12.dp)
                .padding(horizontal = 16.dp),
            onClick = onMoviePicksClick
        )
        TopMoviesPicksList(
            movie = movieCast,
            onNavigateToMoviePicks = onMovieScreenClick
        )
    }
}

@Composable
private fun TvShowsSection(
    tvShows: List<ActorTvShowCastMemberEntity>?,
    onTvShowPicksClick: () -> Unit,
    onTvShowScreenClick: (Int) -> Unit
) {
    tvShows?.takeIf { it.isNotEmpty() }?.let { shows ->
        SectionHeader(
            text = stringResource(R.string.top_tv_shows_picks),
            hasGetAll = true,
            hasIcon = true,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 12.dp)
                .padding(horizontal = 16.dp),
            onClick = onTvShowPicksClick
        )
        TopTvShowsPicksList(
            tvShow = shows,
            onNavigateToTvShowPicks = onTvShowScreenClick
        )
    }
}

@Composable
fun TopMoviesPicksList(
    movie: List<ActorMovieCastMemberEntity>,
    onNavigateToMoviePicks: (Int) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movie.size) { index ->
            HomeCard(
                imageUrl = movie[index].posterUrl,
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                },
                modifier = Modifier.clickable {
                    onNavigateToMoviePicks(movie[index].id)
                }
            )
        }
    }
}

@Composable
fun TopTvShowsPicksList(
    tvShow: List<ActorTvShowCastMemberEntity>,
    onNavigateToTvShowPicks: (Int) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(tvShow.size) { index ->
            HomeCard(
                imageUrl = tvShow[index].posterUrl,
                isSaved = false,
                onSaveClick = {
                    //TODO("Not yet implemented")
                },
                modifier = Modifier.clickable {
                    onNavigateToTvShowPicks(tvShow[index].id)
                }
            )
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
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(16.dp)
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
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = job,
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.body,
            )
            TextWithIcon(
                icon = painterResource(R.drawable.icon_location),
                text = placeOfBirth,
            )
            TextWithIcon(
                icon = painterResource(R.drawable.birthday_cake),
                text = if (deathDay != "") "${birthday.toLocalizedNumbers()}  -  ${deathDay.toLocalizedNumbers()}" else birthday.toLocalizedNumbers(),
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(3.dp)
                .clip(CircleShape)
                .background(NovixTheme.colors.body)
                .align(alignment = Alignment.CenterVertically)
        )
        Icon(
            painter = icon,
            contentDescription = stringResource(R.string.imagr_dot),
            tint = NovixTheme.colors.body,
            modifier = Modifier.size(11.dp)
        )
        Text(
            text = text,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.body,
        )
    }
}

