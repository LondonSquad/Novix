package com.london.presentation.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.london.designsystem.R
import com.london.designsystem.component.EmptySearchComponent
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreenContent(
        state = state,
        interactionListener = viewModel,
        keyboardController = keyboardController,
        viewModel = viewModel
    )
}

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    interactionListener: SearchInteractions,
    viewModel: SearchViewModel,
    keyboardController: SoftwareKeyboardController?,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .height(56.dp),
            title = stringResource(com.london.presentation.R.string.search),
        )

        SearchBar(
            uiState = state,
            viewModel = viewModel,
            interactionSource = interactionSource,
            keyboardController = keyboardController,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (state.searchQuery.text.isNotEmpty()) {
            SearchChipsRow(
                selected = state.selectedCategory,
                onSelect = interactionListener::onCategorySelected,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            when (state.selectedCategory) {
                SearchCategory.Movies -> {
                    if (state.movieResults.isEmpty()) {
                        NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                    } else {
                        MoviesLayOut(
                            movieUis = state.movieResults,
                            onSaveClick = { /* Handle save click */ },
                            isMovieSaved = { false }
                        )
                    }
                }

                SearchCategory.TvShows -> {
                    if (state.tvShowUiResults.isEmpty()) {
                        NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                    } else {
                        TvShowLayOut(
                            tvShowUis = state.tvShowUiResults,
                            onSaveClick = { /* Handle save click */ },
                            isTvShowSaved = { false }
                        )
                    }
                }

                SearchCategory.Actors -> {
                    if (state.actorUiResults.isEmpty()) {
                        NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                    } else {
                        // Todo
                    }
                }
            }
        } else {
            if (state.recentSearches.isNotEmpty()) {
                SectionHeader(
                    text = stringResource(com.london.presentation.R.string.recent_search),
                    hasGetAll = true,
                    hasIcon = false,
                    getAllText = stringResource(com.london.presentation.R.string.clear_all),
                    onClick = interactionListener::clearRecentSearches,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(state.recentSearches) { search ->
                        RecentSearchItem(
                            search = search,
                            onSearchClick = { interactionListener.onRecentSearchClick(search) },
                            onRemoveClick = { interactionListener.removeRecentSearch(search) }
                        )
                    }
                }
            } else {
                NoSearchBeforeLayOut(modifier = Modifier.fillMaxSize())
            }
        }
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
fun TvShowLayOut(
    tvShowUis: List<TvShow>,
    onSaveClick: (TvShow) -> Unit,
    isTvShowSaved: (TvShow) -> Boolean
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
        items(tvShowUis) { tvShow ->
            HomeCard(
                imageUrl = tvShow.posterPicture,
                onSaveClick = { onSaveClick(tvShow) },
                isSaved = isTvShowSaved(tvShow),
                imageDescription = tvShow.name,
            )
        }
    }
}

@Composable
fun MoviesLayOut(
    movieUis: List<Movie>,
    onSaveClick: (Movie) -> Unit,
    isMovieSaved: (Movie) -> Boolean
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
                imageUrl = movie.posterPicture,
                onSaveClick = { onSaveClick(movie) },
                isSaved = isMovieSaved(movie),
                imageDescription = movie.name,
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
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
                    viewModel.addToRecentSearches(uiState.searchQuery.text)
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