package com.london.presentation.screen.moiveDetalis

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onPreviewClick: (Int) -> Unit ={}
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(NovixTheme.colors.surface),
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
    Log.d("tag", "MovieDetails: $state")

    Box(modifier = Modifier.fillMaxSize().background(NovixTheme.colors.surface).padding(vertical = 16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 370.dp)
            ) {
                MovieDetailsImage(
                    isSaved = state.isSaved,
                    onSaveClick = {},
                    images = state.movieImage,
                    currentImageIndex = state.currentImageIndex,
                    direction = state.imageSlideDirection,
                    onBackClick = onBackClick
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NovixCarousalRow(
                        dotsStates = List(state.movieImage.size) { index -> index == state.currentImageIndex },
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
                            .padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            state.movieName,
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
                            stringResource(view_reviews),
                            style = NovixTheme.typography.label.medium,
                            color = NovixTheme.colors.primary,
                            modifier = Modifier.noRippleClickable {
                                onPreviewClick(state.movieId)
                            })
                    }
                }
            }
            Text(
                stringResource(overview),
                style = NovixTheme.typography.label.large,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)

            )
            ConditionalText(
                state.movieOverview, state.expanded, onExpandClick
            )
            Text(
                "Cast",
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(start = 16.dp)
            )
            LazyHorizontalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp , horizontal = 16.dp),
                rows = GridCells.Fixed(1),
            ) {
                items(state.genres) { actor ->
                    ActorItem(
                        actorName = actor.name,
                        characterName = actor.characterName,
                        imageRes = actor.avatarUrl,
                        modifier = Modifier.defaultMinSize(minWidth = 375.dp)
                    )
                }
            }

            Text(
                stringResource(more_like_this),
                style = NovixTheme.typography.title.medium,
                color = NovixTheme.colors.title,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height((state.similarMovies.size / 2 + state.similarMovies.size % 2) * 220.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                items(state.similarMovies) { movie ->
                    HomeCard(
                        imageUrl = movie.image, isSaved = movie.isSaved, onSaveClick = {})
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
            if (state.movieHaveTrailer.not()) {
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
                onClick = {},
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                isDisabled = state.movieHaveTrailer.not(),
                icon = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RatingAndMetaRow(
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
fun IconWithText(
    icon: Int, contentDesc: String, tint: Color, text: String, textColor: Color
) {
    Icon(
        painter = painterResource(icon), contentDescription = contentDesc, tint = tint
    )
    Text(
        text, style = NovixTheme.typography.label.small, color = textColor
    )
}

@Composable
fun GenreRow(genres: List<String>) {
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
fun Dot(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(drawable.ellipse_2),
        contentDescription = stringResource(dot),
        tint = NovixTheme.colors.body,
        modifier = modifier
    )
}

@Composable
fun MovieDetailsImage(
    images: List<Any>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    imageDescription: String? = null,
    loadingState: MutableState<Boolean> = remember { mutableStateOf(false) },
    currentImageIndex: Int,
    direction: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(160f / 100f)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp, color = NovixTheme.colors.stroke, shape = RoundedCornerShape(12.dp)
            ), contentAlignment = Alignment.Center
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

        SaveIcon(
            isSaved = isSaved,
            onSaveClick = onSaveClick,
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(16))
                .align(Alignment.TopEnd)
        )

        ButtonIcon(
            onClick = onBackClick,
            iconRes = R.drawable.arrow_left,
            backgroundColor = NovixTheme.colors.iconBackgroundLow,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(16))
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun ConditionalText(
    text: String,
    expandedState: Boolean,
    onExpandedChange: () -> Unit
) {
    val minimumLineLength = 3
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    var truncatedText by remember { mutableStateOf("") }

    val textStyle = NovixTheme.typography.body.small.copy(color = NovixTheme.colors.body)
    val actionStyle = SpanStyle(
        color = NovixTheme.colors.primary,
        fontSize = NovixTheme.typography.label.medium.fontSize,
        fontWeight = NovixTheme.typography.label.medium.fontWeight
    )

    Text(
        text = buildAnnotatedString {
            when {
                !showReadMoreButtonState -> append(text)
                !expandedState -> {
                    append(truncatedText)
                    withStyle(actionStyle) { append(" Read more") }
                }
                else -> {
                    append(text)
                    withStyle(actionStyle) { append(" Read less") }
                }
            }
        },
        style = textStyle,
        maxLines = if (expandedState || !showReadMoreButtonState) Int.MAX_VALUE else minimumLineLength,
        onTextLayout = { textLayoutResult ->
            if (!showReadMoreButtonState && textLayoutResult.lineCount > minimumLineLength) {
                val lastVisibleLineEndIndex = textLayoutResult.getLineEnd(minimumLineLength - 1)
                val readMoreLength = 15
                var truncateIndex = maxOf(0, lastVisibleLineEndIndex - readMoreLength)

                while (truncateIndex > 0 && text[truncateIndex] != ' ') {
                    truncateIndex--
                }

                truncatedText = text.substring(0, truncateIndex).trim()
                showReadMoreButtonState = true
            }
        },
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (showReadMoreButtonState) onExpandedChange()
            }
    )
}
@Preview(showBackground = true)
@Composable
fun MovieDetailsPreview() {
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
        genres = listOf(
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
