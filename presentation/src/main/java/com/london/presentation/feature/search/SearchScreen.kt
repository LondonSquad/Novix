package com.london.presentation.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.presentation.shared.HomeCard
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.R
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.shared.ActorsLayout
import com.london.presentation.shared.MoviesLayOut
import com.london.presentation.shared.TriangleBlurredShape
import com.london.presentation.shared.TvShowLayOut
import com.london.presentation.utils.Listen
import com.london.presentation.utils.ResultOrEmpty

@Composable
private fun HandleLoadStateError(
    loadState: CombinedLoadStates,
    viewModel: SearchViewModel
) {
    LaunchedEffect(loadState) {
        if (loadState.refresh is LoadState.Error) {
            viewModel.updateSearchState { copy(error = ErrorState.NoInternet) }
        }
    }
}

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToActorDetails: (Int) -> Unit = { },
    onNavigateToTvShowDetails: (Int) -> Unit = { },
    onNavigateToMovieDetails: (Int) -> Unit = { }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is SearchEffect.ActorNavigation -> onNavigateToActorDetails(currentEffect.actorId)
            is SearchEffect.MovieNavigation -> onNavigateToMovieDetails(currentEffect.movieId)
            is SearchEffect.TvNavigation -> onNavigateToTvShowDetails(currentEffect.tvId)
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.updateRecentData()
        }
    }

    val currentPagingFlow = when (state.selectedCategory) {
        SearchCategory.Movies -> state.moviesFlow.collectAsLazyPagingItems()
        SearchCategory.TvShows -> state.tvShowsFlow.collectAsLazyPagingItems()
        SearchCategory.Actors -> state.actorsFlow.collectAsLazyPagingItems()
    }

    BuildScreen(
        isLoading = false,
        isError = currentPagingFlow.loadState.refresh is LoadState.Error,
        onBack = {},
        onRetry = viewModel::onRetry,
        pagingFlow = currentPagingFlow,
        handlePagingLoadingAutomatically = false
    ) {
        SearchScreenContent(
            state = state,
            interactionListener = viewModel,
            keyboardController = keyboardController,
            viewModel = viewModel,
        )
    }
}

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    interactionListener: SearchContract,
    viewModel: SearchViewModel,
    keyboardController: SoftwareKeyboardController?,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .background(color = NovixTheme.colors.surface)
    ) {

        TriangleBlurredShape()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NovixTheme.colors.surface), verticalArrangement = Arrangement.Top
        ) {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                title = stringResource(R.string.search),
            )

            SearchBar(
                uiState = state,
                viewModel = viewModel,
                interactionSource = interactionSource,
                keyboardController = keyboardController,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .fillMaxWidth()
            )

            when {
                state.error != null && state.error != ErrorState.NoInternet -> {
                    ResultOrEmpty(items = state.searchQuery.text.toList(), emptyContent = {
                        ResultOrEmpty(
                            items = state.recentSearches,
                            otherItems = state.recentViewed,
                            emptyContent = {
                                NoEarlierSearchLayout(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(NovixTheme.colors.surface)
                                )
                            },
                            content = {
                                RecentSearchLayOut(
                                    state = state,
                                    interactionListener = interactionListener,
                                    viewModel = viewModel,
                                    onNavigateToTvShowDetails = interactionListener::onTvShowClick,
                                    onNavigateToMovieDetails = interactionListener::onMovieClick
                                )
                            })
                    }, content = {
                        SearchChipsRow(
                            selected = state.selectedCategory,
                            onSelect = interactionListener::onCategorySelected,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    })
                }

                else -> {
                    ResultOrEmpty(items = state.searchQuery.text.toList(), emptyContent = {
                        ResultOrEmpty(
                            items = state.recentSearches,
                            otherItems = state.recentViewed,
                            emptyContent = {
                                NoEarlierSearchLayout(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(NovixTheme.colors.surface)
                                )
                            },
                            content = {
                                RecentSearchLayOut(
                                    state = state,
                                    interactionListener = interactionListener,
                                    viewModel = viewModel,
                                    onNavigateToTvShowDetails = interactionListener::onTvShowClick,
                                    onNavigateToMovieDetails = interactionListener::onMovieClick
                                )
                            })
                    }, content = {
                        SearchChipsRow(
                            selected = state.selectedCategory,
                            onSelect = interactionListener::onCategorySelected,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (state.error == ErrorState.NoInternet) {
                            NetworkErrorScreen(
                                onRetry = {
                                    viewModel.updateSearchState { copy(error = null) }
                                    viewModel.performSearch(
                                        state.searchQuery.text,
                                        state.selectedCategory
                                    )
                                },
                                onBack = null
                            )
                        } else {
                            when (state.selectedCategory) {
                                SearchCategory.Movies -> {
                                    val moviesLazyList = state.moviesFlow.collectAsLazyPagingItems()

                                    SearchContentWithErrorHandling(
                                        moviesLazyList,
                                        viewModel,
                                        state
                                    ) { isLoading ->
                                        ResultOrEmpty(
                                            items = moviesLazyList.itemSnapshotList.items,
                                            emptyContent = {
                                                if (!isLoading) {
                                                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                                                }
                                            },
                                            content = {
                                                MoviesLayOut(
                                                    movieUis = moviesLazyList,
                                                    onSaveClick = { /* Handle save click */ },
                                                    isMovieSaved = { false },
                                                    onMovieClick = {
                                                        viewModel.addToRecentViewed(it.toRecentViewed())
                                                        viewModel.onClickMovie(it.genreIds)
                                                        interactionListener.onMovieClick(it.id)
                                                    },
                                                    modifier = Modifier.padding(horizontal = 16.dp)
                                                )
                                            }
                                        )
                                    }
                                }

                                SearchCategory.TvShows -> {
                                    val tvShowsLazyList =
                                        state.tvShowsFlow.collectAsLazyPagingItems()

                                    SearchContentWithErrorHandling(
                                        tvShowsLazyList,
                                        viewModel,
                                        state
                                    ) { isLoading ->
                                        ResultOrEmpty(
                                            items = tvShowsLazyList.itemSnapshotList.items,
                                            emptyContent = {
                                                if (!isLoading) {
                                                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                                                }
                                            },
                                            content = {
                                                TvShowLayOut(
                                                    tvShowUis = tvShowsLazyList,
                                                    onSaveClick = { /* Handle save click */ },
                                                    isTvShowSaved = { false },
                                                    onTvShowClick = {
                                                        viewModel.addToRecentViewed(it.toRecentViewed())
                                                        it.genres.forEach { genreId ->
                                                            viewModel.incrementGenreInterest(
                                                                genreId,
                                                                "tv"
                                                            )
                                                        }
                                                        viewModel.onTvShowClick(it.id)
                                                    }
                                                )
                                            }
                                        )
                                    }
                                }

                                SearchCategory.Actors -> {
                                    val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()

                                    SearchContentWithErrorHandling(
                                        actorsLazyList,
                                        viewModel,
                                        state
                                    ) { isLoading ->
                                        ResultOrEmpty(
                                            items = actorsLazyList.itemSnapshotList.items,
                                            emptyContent = {
                                                if (!isLoading) {
                                                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                                                }
                                            },
                                            content = {
                                                ActorsLayout(
                                                    items = actorsLazyList, onActorClick = {
                                                        interactionListener.onActorClick(it.id)
                                                    }
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    })
                }
            }

        }


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

    val focusManager = LocalFocusManager.current
    val focusedState = interactionSource.collectIsFocusedAsState().value

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    style = NovixTheme.typography.body.small,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_search_normal),
            trailingIcon = when {
                uiState.searchQuery.text.isNotEmpty()
                        && focusedState -> {
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_remove_filled),
                            contentDescription = stringResource(R.string.clear),
                            tint = NovixTheme.colors.hint,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { viewModel.clearSearch() })
                    }
                }

                else -> null
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.addToRecentSearches(
                        RecentSearch(
                            query = uiState.searchQuery.text,
                            timestamp = System.currentTimeMillis(),
                            id = 0
                        )
                    )
                }),
            interactionSource = interactionSource,
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
private fun SearchChipsRow(
    selected: SearchCategory, onSelect: (SearchCategory) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SearchCategory.entries.forEach { category ->
            NovixChip(
                text = stringResource(category.title),
                isSelected = selected == category,
                onClick = {
                    if (selected != category) {
                        onSelect(category)
                    }
                }
            )
        }
    }
}

@Composable
private fun RecentSearchLayOut(
    state: SearchUiState,
    interactionListener: SearchContract,
    viewModel: SearchViewModel,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit
) {
    if (state.recentViewed.isNotEmpty()) {

        RecentViewedSection(
            recentViewed = state.recentViewed,
            onClearAll = viewModel::clearRecentViewed,
            onNavigateToTvShowDetails = onNavigateToTvShowDetails,
            onNavigateToMovieDetails = onNavigateToMovieDetails
        )
    }

    if (state.recentSearches.isNotEmpty()) {

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        RecentSearchesSection(
            recentSearches = state.recentSearches,
            onClearAll = interactionListener::clearRecentSearches,
            onSearchClick = { query ->
                focusManager.clearFocus()
                keyboardController?.hide()
                interactionListener.onRecentSearchClick(query)
            },
            onRemoveClick = interactionListener::removeRecentSearch
        )
    }
}

@Composable
fun RecentViewedSection(
    recentViewed: List<RecentViewed>,
    onClearAll: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
) {
    SectionHeader(
        text = stringResource(R.string.recent_viewed),
        hasGetAll = true,
        hasIcon = false,
        getAllText = stringResource(R.string.clear_all),
        onClick = onClearAll,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(recentViewed) { item ->
            HomeCard(
                imageUrl = item.imageUrl,
                isSaved = false,
                onSaveClick = { },
                modifier = Modifier.clickable {
                    when (item.type) {
                        MediaType.Movie -> onNavigateToMovieDetails(item.id)
                        MediaType.TvShow -> onNavigateToTvShowDetails(item.id)
                    }
                }
            )
        }
    }
}

@Composable
fun RecentSearchesSection(
    recentSearches: List<RecentSearch>,
    onClearAll: () -> Unit,
    onSearchClick: (String) -> Unit,
    onRemoveClick: (RecentSearch) -> Unit
) {
    SectionHeader(
        text = stringResource(R.string.recent_search),
        hasGetAll = true,
        hasIcon = false,
        getAllText = stringResource(R.string.clear_all),
        onClick = onClearAll,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
    )

    LazyColumn(
        modifier = Modifier
            .background(NovixTheme.colors.surface)
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(recentSearches) { index, search ->
            val isLastItem = index == recentSearches.lastIndex
            RecentSearchItem(
                search = search.query,
                onSearchClick = { onSearchClick(search.query) },
                onRemoveClick = { onRemoveClick(search) },
                showDivider = !isLastItem
            )
        }
    }
}

@Composable
private fun RecentSearchItem(
    search: String,
    onSearchClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.icon_clock),
            contentDescription = stringResource(R.string.clock),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(20.dp)
        )
        Text(
            text = search,
            style = NovixTheme.typography.body.medium,
            color = NovixTheme.colors.title,
            modifier = Modifier
                .padding(end = 4.dp)
                .weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_remove_filled),
            contentDescription = stringResource(R.string.clear),
            tint = NovixTheme.colors.hint,
            modifier = Modifier
                .size(16.dp)
                .clickable { onRemoveClick() })
    }

    if (showDivider) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.5.dp)
                .height(1.dp)
                .background(NovixTheme.colors.stroke)
        )
    }
}

@Composable
private fun NoEarlierSearchLayout(
    modifier: Modifier = Modifier
) {
    EmptyLayout(
        text = stringResource(R.string.start_exploring_msg),
        image = R.drawable.imge_explore,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}


@Composable
private fun NoSearchResultLayOut(
    modifier: Modifier = Modifier
) {
    EmptyLayout(
        text = stringResource(R.string.no_search_result_msg),
        image = R.drawable.img_no_search_result,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun SearchContentWithErrorHandling(
    lazyPagingItems: LazyPagingItems<*>,
    viewModel: SearchViewModel,
    state: SearchUiState,
    content: @Composable (Boolean) -> Unit
) {
    HandleLoadStateError(lazyPagingItems.loadState, viewModel)

    val isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading
    val hasError = lazyPagingItems.loadState.refresh is LoadState.Error

    if (hasError) {
        NetworkErrorScreen(
            onRetry = {
                viewModel.updateSearchState { copy(error = null) }
                viewModel.performSearch(
                    state.searchQuery.text,
                    state.selectedCategory
                )
            },
            onBack = null
        )
    } else {
        content(isLoading)
    }
}

@ThemePreviews
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}