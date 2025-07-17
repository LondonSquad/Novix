package com.london.presentation.screen.details.movieDetalis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.london.designsystem.R
import com.london.designsystem.component.ActorItem
import com.london.designsystem.component.ButtonIcon
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.ImageView
import com.london.designsystem.component.NovixCarousalRow
import com.london.designsystem.component.SaveIcon
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.noRippleClickable
import com.london.presentation.R.drawable
import com.london.presentation.R.string.calendar
import com.london.presentation.R.string.dot
import com.london.presentation.R.string.more_like_this
import com.london.presentation.R.string.overview
import com.london.presentation.R.string.play_trailer
import com.london.presentation.R.string.separator
import com.london.presentation.R.string.star
import com.london.presentation.R.string.time_icon
import com.london.presentation.R.string.view_reviews
import com.london.presentation.composables.ConditionalText
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onPreviewClick: (Int) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NovixTheme.colors.surface),
                contentAlignment = Alignment.Center,
            ) {
                CircularLoading()
            }
        }

        else -> {
            MovieDetailsContent(
                state,
                viewModel::onExpandClick,
                onBackClick,
                onPreviewClick = onPreviewClick
            )
        }
    }
}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsUiState,
    onExpandClick: () -> Unit,
    onBackClick: () -> Unit,
    onPreviewClick: (Int) -> Unit
) {
    val lazyState = rememberLazyListState()
    val isScrolledFarEnough = remember {
        derivedStateOf {
            lazyState.firstVisibleItemIndex > 0 || lazyState.firstVisibleItemScrollOffset > 500
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(1.0f)
                .background(if (isScrolledFarEnough.value) NovixTheme.colors.surface else Color.Transparent)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ),
            contentAlignment = Alignment.Center
        ) {

            SaveIcon(
                isSaved = state.isSaved,
                onSaveClick = {
                    // TODO
                },
                backgroundColor = NovixTheme.colors.iconBackgroundLow,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(16))
                    .align(Alignment.TopEnd),
            )

            ButtonIcon(
                onClick = onBackClick,
                iconRes = R.drawable.arrow_left,
                backgroundColor = NovixTheme.colors.iconBackgroundLow,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(16))
                    .align(Alignment.TopStart),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            state = lazyState
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 370.dp)

                ) {
                    MovieDetailsImage(
                        images = state.movieImage,
                        currentImageIndex = state.currentImageIndex,
                        direction = state.imageSlideDirection,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NovixCarousalRow(
                            dotsStates = List(state.movieImage.size) { index ->
                                index == state.currentImageIndex
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NovixTheme.colors.iconBackgroundLow)
                                .border(1.dp, NovixTheme.colors.stroke, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 158.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NovixTheme.colors.surface)
                                .border(1.dp, NovixTheme.colors.stroke, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = state.movieName,
                                style = NovixTheme.typography.title.medium,
                                color = NovixTheme.colors.title,
                                modifier = Modifier.defaultMinSize(minHeight = 56.dp)
                            )
                            GenreRow(state.movieGenres)
                            RatingAndMetaRow(
                                rate = state.movieRating,
                                time = state.movieDuration,
                                date = state.releaseDate
                            )
                            Text(
                                text = stringResource(view_reviews),
                                style = NovixTheme.typography.label.medium,
                                color = NovixTheme.colors.primary,
                                modifier = Modifier.noRippleClickable {
                                    onPreviewClick(state.movieId)
                                }
                            )
                        }
                    }
                }
            }

            if (state.movieOverview.isNotBlank()) {
                item {
                    Text(
                        text = stringResource(overview),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                    )
                }

                item {
                    ConditionalText(
                        state.movieOverview,
                        state.expanded,
                        onExpandClick
                    )
                }
            }

            if (state.actors.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(com.london.presentation.R.string.cast),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )

                    LazyHorizontalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        rows = GridCells.Fixed(1),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        items(state.actors) { actor ->
                            ActorItem(
                                actorName = actor.name,
                                characterName = actor.characterName,
                                imageRes = actor.avatarUrl,
                                modifier = Modifier.defaultMinSize(minWidth = 296.dp)
                            )
                        }
                    }
                }
            }

            if (state.similarMovies.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(more_like_this),
                        style = NovixTheme.typography.title.medium,
                        color = NovixTheme.colors.title,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                }

                items(state.similarMovies.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { movie ->
                            HomeCard(
                                imageUrl = movie.image,
                                isSaved = movie.isSaved,
                                onSaveClick = {
                                    // TODO
                                },
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f)) // fill empty space if odd item count
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (state.movieHaveTrailer) 16.dp else 24.dp)
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!state.movieHaveTrailer) {
                PrimaryButton(
                    text = null,
                    onClick = {},
                    hasLabel = false,
                    icon = drawable.movie_button_star,
                    hasIcon = true,
                    isLoading = false,
                    isDisabled = false,
                )
            }

            PrimaryButton(
                text = stringResource(play_trailer),
                onClick = {
                    // TODO
                },
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                isDisabled = !state.movieHaveTrailer,
                icon = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun RatingAndMetaRow(
    rate: String,
    time: String,
    date: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWithText(
            icon = drawable.star,
            contentDesc = stringResource(star),
            tint = NovixTheme.colors.yellowAccent,
            text = rate,
            textColor = NovixTheme.colors.title
        )
        Dot()
        IconWithText(
            icon = drawable.time_04,
            contentDesc = stringResource(time_icon),
            tint = NovixTheme.colors.body,
            text = time,
            textColor = NovixTheme.colors.body
        )
        Dot()
        IconWithText(
            icon = drawable.calendar_03,
            contentDesc = stringResource(calendar),
            tint = NovixTheme.colors.body,
            text = date,
            textColor = NovixTheme.colors.body
        )
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
private fun GenreRow(genres: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        genres.forEachIndexed { index, genre ->
            Text(
                genre, style = NovixTheme.typography.label.small, color = NovixTheme.colors.body
            )
            if (index != genres.lastIndex) Icon(
                painter = painterResource(drawable.ellipse_2),
                contentDescription = stringResource(separator),
                tint = NovixTheme.colors.hint
            )
        }
    }
}

@Composable
private fun Dot(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(drawable.ellipse_2),
        contentDescription = stringResource(dot),
        tint = NovixTheme.colors.body,
        modifier = modifier
    )
}

@Composable
private fun MovieDetailsImage(
    images: List<Any>,
    modifier: Modifier = Modifier,
    imageDescription: String? = null,
    loadingState: MutableState<Boolean> = remember { mutableStateOf(false) },
    currentImageIndex: Int,
    direction: Int,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(8f / 5f)
            .clip(RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center
    ) {
        images.forEachIndexed { index, image ->
            AnimatedVisibility(
                visible = currentImageIndex == index,
                enter = slideInHorizontally(
                    initialOffsetX = { if (direction > 0) it else -it },
                    animationSpec = tween(durationMillis = 1000)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { if (direction > 0) -it else it },
                    animationSpec = tween(durationMillis = 1000)
                ),
            ) {
                ImageView(
                    model = image,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    onLoadingStateChange = { loadingState.value = it },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsPreview() {
    val fakeState = MovieDetailsUiState(
        movieImage = listOf(
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg"

        ),
        movieName = "The Shawshank Redemption",
        movieGenres = listOf("Drama", "Crime", "Classic"),
        movieRating = "9.9",
        movieDuration = "2h 22m",
        releaseDate = "1994-09-22",
        movieOverview = "It is a 1994 American drama film, considered one of the greatest films in cinematic history. It revolves around Andy Dufresne, a banker wrongfully convicted of the murder of his wife and",
        actors = listOf(
            ActorUIState(
                "Tim Robbins",
                characterName = "Andy Dufresne",
                avatarUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg"
            ), ActorUIState(
                "Morgan Freeman",
                characterName = "Red",
                avatarUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg"
            )
        ),
        similarMovies = listOf(
            SimilarMovieUIState(
                "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg", false
            ), SimilarMovieUIState(
                "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg", true
            )
        ),
        isRated = true,
        movieHaveTrailer = true
    )

    NovixTheme {
        MovieDetailsContent(state = fakeState, {}, {}, {})
    }
}
