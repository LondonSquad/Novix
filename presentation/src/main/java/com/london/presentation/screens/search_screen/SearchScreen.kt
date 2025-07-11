package com.london.presentation.screens.search_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.R
import com.london.designsystem.component.EmptySearchComponent
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.screens.search_screen.model.MovieUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        state = state,
        interactionListener = viewModel,
        isMovieSaved = { viewModel.isMovieSaved(it) }
    )
}

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    interactionListener: SearchInteractions,
    isMovieSaved: (MovieUi) -> Boolean
) {
    var searchQuery by remember { mutableStateOf(state.searchQuery) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(searchQuery)) }
    val interactionSource = remember { MutableInteractionSource() }
    val results = when (state.selectedCategory) {
        SearchCategory.Movies -> state.movieResults
        SearchCategory.TvShows -> state.tvShowUiResults
        SearchCategory.Actors -> state.actorUiResults
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        SearchTopBar(title = "Search")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    state.searchQuery = it.text
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                leadingIcon = painterResource(R.drawable.icon_search),
                interactionSource = interactionSource
            )

            PrimaryButton(
                hasIcon = true,
                icon = R.drawable.filter_horizontal,
                onClick = { state.showFilterBottomSheet = true },
                hasLabel = false,
                isLoading = false,
                isDisabled = false,
                text = null,
                modifier = Modifier
            )
        }

        when {
            state.searchQuery.isEmpty() && state.searchHistory.isEmpty() -> {
                NoSearchBeforeLayOut(modifier = Modifier.weight(1f))
                return@Column
            }

            state.searchQuery.isEmpty() && state.searchHistory.isNotEmpty() -> {
                TODO("SearchHistoryScreen()")
            }
        }

        SearchChipsRow(
            selected = state.selectedCategory,
            onSelect = { interactionListener.onCategorySelected(it) },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AnimatedContent(
            targetState = Pair(state.selectedCategory, results.isEmpty()),
            label = "CategorySearchContentTransition",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { (category, isEmpty) ->
            if (isEmpty) {
                NoSearchResultLayOut(
                    modifier = Modifier.fillMaxSize()
                )
                return@AnimatedContent
            }
            when (category) {
                SearchCategory.Movies -> CategoryContent(
                    items = state.movieResults,
                    content = { movies ->
                        MoviesLayOut(
                            movieUis = movies,
                            onSaveMovie = { interactionListener.onSavedMovieClick(it) },
                            isMovieSaved = isMovieSaved
                        )
                    }
                )

                SearchCategory.TvShows -> CategoryContent(
                    items = state.tvShowUiResults,
                    content = { tvShows -> TODO("TvShowsLayOut()") }
                )

                SearchCategory.Actors -> CategoryContent(
                    items = state.actorUiResults,
                    content = { actors -> TODO("ActorsLayOut()") }
                )
            }
        }

        if (state.showFilterBottomSheet) {
            TODO("FilterBottomSheet()")
        }
    }
}

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    title: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = title,
            style = NovixTheme.typography.title.large,
            color = NovixTheme.colors.title,
            modifier = Modifier,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SearchChipsRow(
    selected: SearchCategory,
    onSelect: (SearchCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        NovixChip(
            text = SearchCategory.Movies.title,
            isSelected = selected == SearchCategory.Movies,
            onClick = { onSelect(SearchCategory.Movies) }
        )
        NovixChip(
            text = SearchCategory.TvShows.title,
            isSelected = selected == SearchCategory.TvShows,
            onClick = { onSelect(SearchCategory.TvShows) }
        )
        NovixChip(
            text = SearchCategory.Actors.title,
            isSelected = selected == SearchCategory.Actors,
            onClick = { onSelect(SearchCategory.Actors) }
        )
    }
}

@Composable
fun MoviesLayOut(
    movieUis: List<MovieUi>,
    onSaveMovie: (MovieUi) -> Unit,
    isMovieSaved: (MovieUi) -> Boolean
) {
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val itemWidthPx = with(LocalDensity.current) { 158.dp.toPx() }
    val screenPaddingPx = with(LocalDensity.current) { 32.dp.toPx() }
    val columns = ((screenWidth - screenPaddingPx) / itemWidthPx).toInt().coerceAtLeast(2)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movieUis) { movie ->
            HomeCard(
                imageUrl = movie.posterUrl,
                onSaveClick = { onSaveMovie(movie) },
                isSaved = isMovieSaved(movie),
                imageDescription = movie.title
            )
        }
    }
}


@Composable
fun NoSearchBeforeLayOut(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        EmptySearchComponent(
            text = "Start exploring! Search for your favorite movies, series and shows",
            image = R.drawable.img_explore,
        )
    }
}

@Composable
fun NoSearchResultLayOut(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        EmptySearchComponent(
            text = "No search result, please try with another keyword!",
            image = R.drawable.img_no_search_result,
        )
    }
}

@ThemePreviews
@Composable
fun SearchPrev() {
    NovixTheme {
    }
}