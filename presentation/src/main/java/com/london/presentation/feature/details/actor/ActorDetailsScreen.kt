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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.ErrorImage
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.actordetails.cast.ActorMediaItems
import com.london.presentation.R
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.ImageView
import com.london.presentation.shared.TextWithIcon
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun ActorDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGallery: (Int) -> Unit,
    onNavigateToTopMoviePicks: (Int) -> Unit,
    onNavigateToMovieScreen: (Int) -> Unit,
    onNavigateToTopTvShowPicks: (Int) -> Unit,
    onNavigateToTvShowScreen: (Int) -> Unit,
    viewModel: ActorDetailsViewModel = hiltViewModel(),
) {
    val effect by viewModel.effect.collectAsState(null)
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is ActorEffect.BackNavigation -> onNavigateBack()
            is ActorEffect.GalleryNavigation -> onNavigateToGallery(currentEffect.actorId)
            is ActorEffect.TopMoviePicksNavigation -> onNavigateToTopMoviePicks(uiState.actorDetails.id)
            is ActorEffect.MovieScreenNavigation -> onNavigateToMovieScreen(currentEffect.movieId)
            is ActorEffect.TopTvShowPicksNavigation -> onNavigateToTopTvShowPicks(uiState.actorDetails.id)
            is ActorEffect.TvShowScreenNavigation -> onNavigateToTvShowScreen(currentEffect.tvShowId)
        }
    }

    BuildScreen(
        isLoading = uiState.isLoading,
        isError = uiState.error != null,
        onBack = viewModel::onBackClick,
        onRetry = viewModel::onRetryClick
    ) {
        Content(
            uiState = uiState,
            actorDetailsContract = viewModel
        )
    }
}


@Composable
private fun Content(
    uiState: ActorDetailsUiState,
    actorDetailsContract: ActorDetailsContract,
) {

    val lazyState = rememberLazyListState()
    val shouldShowBackground by remember {
        derivedStateOf {
            lazyState.firstVisibleItemScrollOffset > 40f || lazyState.firstVisibleItemIndex > 0
        }
    }
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (shouldShowBackground) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "background_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        EmptyScreen(uiState)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 16.dp
            ),
            state = lazyState
        ) {
            item {
                if (hasOtherContent(uiState)) {
                    uiState.actorImageDetails?.let { image ->
                        CustomBackDropImagePager(
                            images = image,
                            isVisibleDots = false
                        )
                    }
                }
            }

            item { ActorInfoSectionItem(uiState = uiState) }
            item { BiographySection(uiState = uiState) }
            item {
                GallerySection(
                    images = uiState.actorImageDetails,
                    onGalleryClick = {
                        actorDetailsContract.onActorGalleryClick(uiState.actorDetails.id)
                    }
                )
            }
            item {
                MoviesSection(
                    movies = uiState.actorMovieDetails?.mediaItems,
                    onTopMoviePicksClick = {
                        actorDetailsContract.onTopMoviePicksClick(uiState.actorDetails.id)
                    },
                    onMovieScreenClick = actorDetailsContract::onMovieScreenClick
                )
            }
            item {
                TvShowsSection(
                    tvShows = uiState.actorTvShowDetails?.mediaItems,
                    onTopTvShowPicksClick = {
                        actorDetailsContract.onTopTvShowPicksClick(uiState.actorDetails.id)
                    },
                    onTvShowScreenClick = actorDetailsContract::onTvShowScreenClick
                )
            }
        }

        TopBar(
            onBackClick = actorDetailsContract::onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .background(NovixTheme.colors.surface.copy(alpha = backgroundAlpha))
                .padding(
                    start = 16.dp,
                    top = WindowInsets.statusBars.asPaddingValues()
                        .calculateTopPadding() + 12.dp
                )
                .zIndex(1f)
        )
    }
}

@Composable
private fun ActorInfoSectionItem(uiState: ActorDetailsUiState) {
    with(uiState) {
        if (actorDetails.name.isNotEmpty()
            || actorDetails.birthday.isNotBlank()
            || actorDetails.placeOfBirth.isNotBlank()
            || actorDetails.knownForDepartment.isNotBlank()
        ) {
            ActorInfoSection(
                job = actorDetails.knownForDepartment,
                name = actorDetails.name,
                birthday = actorDetails.birthday,
                deathDay = actorDetails.deathDay ?: "",
                placeOfBirth = actorDetails.placeOfBirth,
            )
        }
    }
}

@Composable
private fun BiographySection(uiState: ActorDetailsUiState) {
    if (uiState.actorDetails.biography.isNotBlank()) {
        Text(
            text = stringResource(R.string.biography),
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        var isExpanded by remember { mutableStateOf(false) }
        ConditionalText(
            text = uiState.actorDetails.biography,
            expandedState = isExpanded,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
        ) {
            isExpanded = !isExpanded
        }
    }
}

@Composable
private fun GallerySection(
    images: List<String>?,
    onGalleryClick: () -> Unit
) {
    if (!images.isNullOrEmpty()) {
        SectionHeader(
            text = stringResource(R.string.gallery),
            hasGetAll = true,
            hasIcon = true,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            onClick = onGalleryClick
        )
        ActorGallery(images = images)
    }
}

@Composable
private fun MoviesSection(
    movies: List<ActorMediaItems>?,
    onTopMoviePicksClick: () -> Unit,
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
            onClick = onTopMoviePicksClick
        )
        TopMoviesPicksList(
            movie = movieCast,
            onNavigateToMoviePicks = onMovieScreenClick
        )
    }
}

@Composable
private fun TvShowsSection(
    tvShows: List<ActorMediaItems>?,
    onTopTvShowPicksClick: () -> Unit,
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
            onClick = onTopTvShowPicksClick
        )
        TopTvShowsPicksList(
            tvShow = shows,
            onNavigateToTvShowPicks = onTvShowScreenClick
        )
    }
}

@Composable
fun TopMoviesPicksList(
    movie: List<ActorMediaItems>,
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
                onSaveClick = { /* TODO: Not yet implemented */ },
                modifier = Modifier.clickable {
                    onNavigateToMoviePicks(movie[index].id)
                }
            )
        }
    }
}

@Composable
fun TopTvShowsPicksList(
    tvShow: List<ActorMediaItems>,
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
                onSaveClick = { /* TODO: Not yet implemented */ },
                modifier = Modifier.clickable {
                    onNavigateToTvShowPicks(tvShow[index].id)
                }
            )
        }
    }
}

@Composable
fun ActorGallery(images: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(88.dp)
    ) {
        itemsIndexed(images) { _, imageDetails ->
            ImageView(
                model = imageDetails,
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
                loadingContent = { CircularLoading(modifier = Modifier) }
            )
        }
    }
}

@Composable
private fun ActorInfoSection(
    job: String,
    name: String,
    birthday: String,
    deathDay: String,
    placeOfBirth: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .offsetLayout()
            .fillMaxWidth()
            .defaultMinSize(minHeight = 132.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NovixTheme.colors.surface)
            .border(1.dp, NovixTheme.colors.stroke, RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${name}\n",
            style = NovixTheme.typography.title.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp),
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
                text = if (deathDay.isNotEmpty()) "${birthday.toLocalizedNumbers()}  -  ${deathDay.toLocalizedNumbers()}" else birthday.toLocalizedNumbers(),
            )
        }
    }
}

@Composable
private fun EmptyScreen(uiState: ActorDetailsUiState) {
    if (uiState.isLoading || uiState.error != null) return

    val hasNoContent = uiState.actorImageDetails.isNullOrEmpty() &&
            uiState.actorMovieDetails?.mediaItems.isNullOrEmpty() &&
            uiState.actorTvShowDetails?.mediaItems.isNullOrEmpty() &&
            uiState.actorDetails.biography.isBlank() &&
            (uiState.actorDetails.name.isBlank() &&
                    uiState.actorDetails.birthday.isBlank() &&
                    uiState.actorDetails.placeOfBirth.isBlank() &&
                    uiState.actorDetails.knownForDepartment.isBlank())

    if (hasNoContent) {
        EmptyLayout(
            text = stringResource(R.string.no_actor_details),
            image = R.drawable.img_no_result,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun hasOtherContent(uiState: ActorDetailsUiState): Boolean {
    return uiState.actorDetails.name.isNotBlank() ||
            uiState.actorDetails.birthday.isNotBlank() ||
            uiState.actorDetails.placeOfBirth.isNotBlank() ||
            uiState.actorDetails.knownForDepartment.isNotBlank() ||
            uiState.actorDetails.biography.isNotBlank() ||
            !uiState.actorMovieDetails?.mediaItems.isNullOrEmpty() ||
            !uiState.actorTvShowDetails?.mediaItems.isNullOrEmpty()
}

@Preview
@Composable
fun Preview() {
    NovixTheme {
        ActorDetailsScreen(
            onNavigateBack = {},
            onNavigateToTopMoviePicks = {},
            onNavigateToGallery = {},
            onNavigateToTopTvShowPicks = {},
            onNavigateToMovieScreen = {},
            onNavigateToTvShowScreen = {}
        )
    }
}
