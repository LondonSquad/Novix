package com.london.presentation.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.london.designsystem.R
import com.london.designsystem.component.EmptySearchComponent
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.screen.search.model.MovieUi

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

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
    var textFieldValue by remember { mutableStateOf(state.searchQuery) }
    val interactionSource = remember { MutableInteractionSource() }
    val results = remember {
        when (state.selectedCategory) {
            SearchCategory.Movies -> state.movieResults
            SearchCategory.TvShows -> state.tvShowUiResults
            SearchCategory.Actors -> state.actorUiResults
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
//        TopBar(
//            modifier = Modifier
//                .statusBarsPadding()
//                .height(56.dp)
//                .padding(horizontal = 16.dp),
//            title = stringResource(com.london.presentation.R.string.search),
//        )

//        SearchBar(uiState, viewModel, interactionSource, keyboardController)

        SearchTopBar(title = "Search")

        if (state.searchQuery.text.isEmpty() && state.searchHistory.isEmpty()) {
            NoSearchBeforeLayOut(modifier = Modifier.fillMaxSize())
            return
        }


        // Handle search history case if needed
        if (state.searchQuery.text.isEmpty() && state.searchHistory.isNotEmpty()) {
            when {
                state.searchQuery.text.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TO DO: No search result",
                            style = NovixTheme.typography.body.medium,
                            color = NovixTheme.colors.hint
                        )
                    }
                }

                state.searchQuery.text.isEmpty() -> {
                    if (state.recentViewed.isNotEmpty()) {
                        SectionHeader(
                            text = stringResource(com.london.presentation.R.string.recent_viewed),
                            hasGetAll = true,
                            hasIcon = false,
                            getAllText = stringResource(com.london.presentation.R.string.clear_all),
                            onClick = { viewModel.clearRecentViewed() },
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                        ) {
                            items(state.recentViewed) { imageUrl ->
                                HomeCard(
                                    imageUrl = imageUrl,
                                    isSaved = false,
                                    onSaveClick = {}
                                )
                            }
                        }
                    }

                    if (state.recentSearches.isNotEmpty()) {
                        SectionHeader(
                            text = stringResource(com.london.presentation.R.string.recent_search),
                            hasGetAll = true,
                            hasIcon = false,
                            getAllText = stringResource(com.london.presentation.R.string.clear_all),
                            onClick = { viewModel.clearRecentSearches() },
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items(state.recentSearches) { search ->
                                RecentSearchItem(
                                    search = search,
                                    onSearchClick = { viewModel.onRecentSearchClick(search) },
                                    onRemoveClick = { viewModel.removeRecentSearch(search) }
                                )
                            }
                        }
                    }
                }
            }
        }

        SearchChipsRow(
            selected = state.selectedCategory,
            onSelect = { searchCategory ->
                interactionListener.onCategorySelected(searchCategory)
            },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when (state.selectedCategory) {
            SearchCategory.Movies -> {
                if (state.movieResults.isEmpty()) {
                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                } else {
                    MoviesLayOut(
                        movieUis = state.movieResults,
                        onSaveMovie = { interactionListener.onSavedMovieClick(it) },
                        isMovieSaved = isMovieSaved,
                    )
                }
            }

            SearchCategory.TvShows -> {
                // Just show placeholder for now
                Text(
                    text = "TV Shows - Coming Soon",
                    style = NovixTheme.typography.body.medium,
                    color = NovixTheme.colors.title,
                    modifier = Modifier.padding(16.dp)
                )
            }

            SearchCategory.Actors -> {
                // Just show placeholder for now
                Text(
                    text = "Actors - Coming Soon",
                    style = NovixTheme.typography.body.medium,
                    color = NovixTheme.colors.title,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (state.showFilterBottomSheet) {

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
                imageDescription = movie.title,
            )
        }
    }
}


@Composable
fun NoSearchBeforeLayOut(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (emptySearch) = createRefs()
        EmptySearchComponent(
            text = "Start exploring! Search for your favorite movies, series and shows",
            image = R.drawable.img_explore,
            modifier = Modifier.constrainAs(emptySearch) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun NoSearchResultLayOut(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (emptySearch) = createRefs()

        EmptySearchComponent(
            text = "No search result, please try with another keyword!",
            image = R.drawable.img_no_search_result,
            modifier = Modifier.constrainAs(emptySearch) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}


@Composable
private fun SearchBar(
    uiState: SearchUiState,
    viewModel: SearchViewModel,
    interactionSource: MutableInteractionSource,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = {
                Text(
                    stringResource(com.london.presentation.R.string.search_placeholder),
                    style = NovixTheme.typography.body.small,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            leadingIcon = painterResource(id = com.london.presentation.R.drawable.icon_search_normal),
            trailingIcon = when {
                uiState.searchQuery.text.isNotEmpty() -> {
                    {
                        Icon(
                            painter = painterResource(id = com.london.presentation.R.drawable.icon_remove_filled),
                            contentDescription = stringResource(com.london.presentation.R.string.clear),
                            tint = NovixTheme.colors.hint,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { viewModel.clearSearch() }
                        )
                    }
                }

                else -> null
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        PrimaryButton(
            text = "",
            onClick = {},
            isLoading = false,
            isDisabled = false,
            hasIcon = true,
            icon = com.london.presentation.R.drawable.icon_filter,
            hasLabel = false,
            modifier = Modifier.width(52.dp)
        )
    }
}

@Composable
private fun RecentSearchItem(
    search: String,
    onSearchClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = com.london.presentation.R.drawable.icon_clock),
            contentDescription = stringResource(com.london.presentation.R.string.clock),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp, bottom = 2.dp, end = 8.dp)
        )
        Text(
            text = search,
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Icon(
            painter = painterResource(id = com.london.presentation.R.drawable.icon_remove_filled),
            contentDescription = stringResource(com.london.presentation.R.string.clear),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .clickable { onRemoveClick() }
                .padding(vertical = 4.dp)
        )
    }
}

@ThemePreviews
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}