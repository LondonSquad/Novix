package com.london.presentation.feature.details.movieDetalis

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.R
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.Icon
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable
import com.london.presentation.R.drawable
import com.london.presentation.R.string.calendar
import com.london.presentation.R.string.more_like_this
import com.london.presentation.R.string.overview
import com.london.presentation.R.string.star
import com.london.presentation.R.string.time_icon
import com.london.presentation.R.string.view_reviews
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.reviews.MediaType
import com.london.presentation.feature.search.SearchCategory
import com.london.presentation.shared.ConditionalText
import com.london.presentation.shared.CustomBackDropImagePager
import com.london.presentation.shared.FooterSection
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertGenreCodeToString
import com.london.presentation.utils.getLocalizedTimeUnit
import com.london.presentation.utils.gridColmuns
import com.london.presentation.utils.isNotZeroRate
import com.london.presentation.utils.offsetLayout
import com.london.presentation.utils.openUrl
import com.london.presentation.utils.reverseDateFormat
import com.london.presentation.utils.toLocalizedNumbers

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateGenre: (Int) -> Unit = {},
    onNavigateToMovie: (Int) -> Unit,
    onNavigateToActor: (Int) -> Unit,
    onNavigateToReviews: (Int, Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    HandleMovieDetailsEffects(
        effect = effect,
        onNavigateBack = onNavigateBack,
        onNavigateGenre = onNavigateGenre,
        onNavigateToMovie = onNavigateToMovie,
        onNavigateToActor = onNavigateToActor,
        onNavigateToReviews = onNavigateToReviews
    )

    BuildScreen(
        onBack = viewModel::onBackClick,
        isLoading = state.isLoading,
        isError = state.error != null,
        onRetry = viewModel::onRetry
    ) {
        MovieDetailsContent(
            uiState = state,
            movieDetailsContract = viewModel
        )
    }
}

@Composable
fun MovieDetailsContent(
    uiState: MovieDetailsUiState,
    movieDetailsContract: MovieDetailsContract
) {
    val screenWidthDp =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    val uriHandler = LocalUriHandler.current

    val lazyState = rememberLazyGridState()
    var footerHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

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
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {

        TopBar(
            onBackClick = movieDetailsContract::onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    NovixTheme.colors.surface.copy(alpha = backgroundAlpha)
                )
                .padding(horizontal = 16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                ),
            onClickOption1 = { /*todo on click on save*/ },
            option1Icon = R.drawable.icon_remove,
        )

        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp + footerHeight),
            state = lazyState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Fixed(gridColmuns()),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 370.dp)

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CustomBackDropImagePager(
                            images = uiState.movieImages,
                            modifier = Modifier.requiredWidth(screenWidthDp)
                        )

                        Column(
                            modifier = Modifier
                                .offsetLayout()
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 158.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NovixTheme.colors.surface)
                                .border(1.dp, NovixTheme.colors.stroke, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = uiState.movieName,
                                style = NovixTheme.typography.title.medium,
                                color = NovixTheme.colors.title,
                                modifier = Modifier.defaultMinSize(minHeight = 56.dp)
                            )
                            GenreRow(uiState.movieGenres, movieDetailsContract::onGenreClick)
                            RatingAndMetaRow(
                                rate = uiState.movieRating,
                                time = uiState.movieDuration,
                                date = uiState.releaseDate
                            )
                            Text(
                                text = stringResource(view_reviews),
                                style = NovixTheme.typography.label.medium,
                                color = NovixTheme.colors.primary,
                                modifier = Modifier.noRippleClickable {
                                    movieDetailsContract.onReviewsClick(
                                        uiState.movieId,
                                        MediaType.Movie.mediaNum
                                    )
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.movieOverview.isNotBlank()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(overview),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    ConditionalText(
                        uiState.movieOverview,
                        uiState.expanded,
                        onExpandedChange = movieDetailsContract::onExpandClick
                    )
                }
            }

            if (uiState.actors.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(com.london.presentation.R.string.cast),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                    )

                    LazyHorizontalGrid(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .requiredWidth(screenWidthDp)
                            .height(100.dp),
                        rows = GridCells.Fixed(1),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        itemsIndexed(uiState.actors) { _, actor ->
                            ActorItem(
                                actorName = actor.name,
                                characterName = actor.characterName,
                                imageRes = actor.profilePictureUrl,
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 296.dp),
                                onClick = {
                                    movieDetailsContract.onActorClick(
                                        actor.id
                                    )
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.similarMovies.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(more_like_this),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(uiState.similarMovies) { movie ->
                    HomeCard(
                        imageUrl = movie.posterUrl,
                        isSaved = false,
                        onSaveClick = {},
                        modifier = Modifier
                            .clickable {
                                movieDetailsContract.onMovieClick(movie.id)
                            }
                    )
                }
            }
        }

        FooterSection(
            haveTrailer = uiState.movieHaveTrailer,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    footerHeight = with(density) { coordinates.size.height.toDp() }
                }
                .align(Alignment.BottomCenter),
            onPlayClick = {
                uriHandler.openUrl(uiState.movieVideo)
            },
            onStarClick = {
                // TODO save favorite onclick handler
            }
        )
    }
}

@Composable
private fun RatingAndMetaRow(
    rate: String?,
    time: String?,
    date: String?,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!rate.isNullOrBlank() && rate.isNotZeroRate()) {
            IconWithText(
                icon = drawable.star,
                contentDesc = stringResource(star),
                tint = NovixTheme.colors.yellowAccent,
                text = rate.toLocalizedNumbers(),
                textColor = NovixTheme.colors.body
            )
        }

        if (!time.isNullOrBlank() || !date.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(NovixTheme.colors.body)
            )
        }

        if (!time.isNullOrBlank() && time != "0") {
            val timeInt = time.toInt()
            IconWithText(
                icon = drawable.time_04,
                contentDesc = stringResource(time_icon),
                tint = NovixTheme.colors.body,
                text = "${(timeInt / 60).toLocalizedNumbers()}${getLocalizedTimeUnit("h")} ${(timeInt % 60).toLocalizedNumbers()}${
                    getLocalizedTimeUnit(
                        "m"
                    )
                }",
                textColor = NovixTheme.colors.body
            )
        }
        val showDot =
            !time.isNullOrBlank() && time != "0" && !date.isNullOrBlank() && !rate.isNullOrBlank()

        if (showDot) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(NovixTheme.colors.body)
            )
        }


        if (!date.isNullOrBlank()) {
            IconWithText(
                icon = drawable.calendar_03,
                contentDesc = stringResource(calendar),
                tint = NovixTheme.colors.body,
                text = reverseDateFormat(date),
                textColor = NovixTheme.colors.body
            )
        }
    }
}


@Composable
private fun HandleMovieDetailsEffects(
    effect: MovieDetailsEffect?,
    onNavigateBack: () -> Unit,
    onNavigateGenre: (Int) -> Unit,
    onNavigateToMovie: (Int) -> Unit,
    onNavigateToActor: (Int) -> Unit,
    onNavigateToReviews: (Int, Int) -> Unit,
) {
    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is MovieDetailsEffect.ActorNavigation -> onNavigateToActor(currentEffect.actorId)
            MovieDetailsEffect.BackNavigation -> onNavigateBack()
            is MovieDetailsEffect.GenreNavigation -> onNavigateGenre(currentEffect.genreId)
            is MovieDetailsEffect.MovieNavigation -> onNavigateToMovie(currentEffect.movieId)
            is MovieDetailsEffect.ReviewsNavigation -> onNavigateToReviews(
                currentEffect.movieId,
                currentEffect.mediaNumber
            )
        }
    }
}


@Composable
private fun IconWithText(
    icon: Int,
    contentDesc: String,
    tint: Color,
    text: String,
    textColor: Color
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDesc,
        tint = tint
    )
    Text(
        text,
        style = NovixTheme.typography.label.small,
        color = textColor
    )
}


@Composable
private fun GenreRow(
    genres: List<Int>,
    onGenreClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        genres.forEachIndexed { index, genre ->
            Text(
                stringResource(convertGenreCodeToString(genre, SearchCategory.Movies)),
                style = NovixTheme.typography.label.small,
                color = NovixTheme.colors.body,
                modifier = Modifier.noRippleClickable {
                    onGenreClick(genre)
                }
            )
            if (index != genres.lastIndex)
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(NovixTheme.colors.hint)
                )
        }
    }
}
