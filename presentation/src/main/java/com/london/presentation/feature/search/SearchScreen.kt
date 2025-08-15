package com.london.presentation.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.search.composable.RecentSearchesSection
import com.london.presentation.feature.search.composable.RecentViewedSection
import com.london.presentation.feature.search.composable.SearchBar
import com.london.presentation.feature.search.composable.SearchChipsRow
import com.london.presentation.shared.ActorsLayout
import com.london.presentation.shared.TriangleBlurredShape
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.buildscreen.NetworkErrorScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.ResultOrEmpty
import com.london.presentation.utils.toRecentViewed

@Composable
fun SearchScreen(
    onNavigateToActorDetails: (Int) -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is SearchEffect.ToActorNavigation -> onNavigateToActorDetails(currentEffect.actorId)
            is SearchEffect.ToMovieNavigation -> onNavigateToMovieDetails(currentEffect.movieId)
            is SearchEffect.ToTvShowNavigation -> onNavigateToTvShowDetails(currentEffect.tvId)
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.updateRecentData()
        }
    }
    Content(
        state = state,
        contract = viewModel,
        keyboardController = keyboardController,
    )
}

@Composable
private fun Content(
    state: SearchUiState,
    contract: SearchContract,
    keyboardController: SoftwareKeyboardController?,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    val currentPagingFlow = when (state.selectedCategory) {
        SearchCategory.Movies -> state.moviesFlow.collectAsLazyPagingItems()
        SearchCategory.TvShows -> state.tvShowsFlow.collectAsLazyPagingItems()
        SearchCategory.Actors -> state.actorsFlow.collectAsLazyPagingItems()
    }

    BuildScreen(
        isLoading = false,
        isError = currentPagingFlow.loadState.refresh is LoadState.Error,
        onBack = {},
        onRetry = contract::onRetryClick,
        pagingFlow = currentPagingFlow,
        handlePagingLoadingAutomatically = false
    ) {
        SearchBody(
            state = state,
            contract = contract,
            interactionSource = interactionSource,
            keyboardController = keyboardController,
            onClearFocus = { focusManager.clearFocus() }
        )
    }
}

@Composable
private fun SearchBody(
    state: SearchUiState,
    contract: SearchContract,
    interactionSource: MutableInteractionSource,
    keyboardController: SoftwareKeyboardController?,
    onClearFocus: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { onClearFocus() }) }
            .background(color = NovixTheme.colors.surface)
    ) {
        TriangleBlurredShape()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NovixTheme.colors.surface),
            verticalArrangement = Arrangement.Top
        ) {
            SearchMainContent(
                state = state,
                contract = contract,
                interactionSource = interactionSource,
                keyboardController = keyboardController
            )
        }
    }
}

@Composable
private fun SearchMainContent(
    state: SearchUiState,
    contract: SearchContract,
    interactionSource: MutableInteractionSource,
    keyboardController: SoftwareKeyboardController?,
) {
    TopBar(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        title = stringResource(R.string.search),
    )

    SearchBar(
        uiState = state,
        contract = contract,
        interactionSource = interactionSource,
        keyboardController = keyboardController,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .fillMaxWidth()
    )

    SearchErrorOrResults(state = state, contract = contract)
}

@Composable
private fun SearchErrorOrResults(
    state: SearchUiState,
    contract: SearchContract
) {
    when {
        state.error != null && state.error != ErrorState.NoInternet -> {
            SearchContentWithError(state = state, contract = contract)
        }

        else -> {
            SearchResultsContent(
                state = state,
                contract = contract
            )
        }
    }
}

@Composable
private fun SearchContentWithError(
    state: SearchUiState,
    contract: SearchContract
) {
    ResultOrEmpty(
        items = state.searchQuery.text.toList(),
        emptyContent = { SearchRecentArea(state = state, contract = contract) },
        content = {
            SearchChipsRow(
                selected = state.selectedCategory,
                onSelect = contract::onCategorySelected,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    )
}

@Composable
private fun SearchRecentArea(
    state: SearchUiState,
    contract: SearchContract
) {
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
                contract = contract,
                onNavigateToTvShowDetails = contract::onTvShowClick,
                onNavigateToMovieDetails = contract::onMovieClick
            )
        }
    )
}

@Composable
private fun SearchResultsContent(
    state: SearchUiState,
    contract: SearchContract
) {
    ResultOrEmpty(
        items = state.searchQuery.text.toList(),
        emptyContent = {
            RecentSearchesContent(
                state = state,
                contract = contract
            )
        },
        content = {
            SearchResultsWithCategory(
                state = state,
                contract = contract
            )
        }
    )
}

@Composable
private fun RecentSearchesContent(
    state: SearchUiState,
    contract: SearchContract
) {
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
                contract = contract,
                onNavigateToTvShowDetails = contract::onTvShowClick,
                onNavigateToMovieDetails = contract::onMovieClick
            )
        }
    )
}

@Composable
private fun SearchResultsWithCategory(
    state: SearchUiState,
    contract: SearchContract
) {
    SearchChipsRow(
        selected = state.selectedCategory,
        onSelect = contract::onCategorySelected,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    if (state.error == ErrorState.NoInternet) {
        NetworkErrorScreen(
            onRetry = {
                contract.updateSearchState { copy(error = null) }
                contract.performSearch(
                    state.searchQuery.text,
                    state.selectedCategory
                )
            },
            onBack = null
        )
    } else {
        SearchContentByCategory(
            state = state,
            contract = contract
        )
    }
}

@Composable
private fun SearchContentByCategory(
    state: SearchUiState,
    contract: SearchContract
) {
    when (state.selectedCategory) {
        SearchCategory.Movies -> MovieSearchContent(state, contract)
        SearchCategory.TvShows -> TvShowSearchContent(state, contract)
        SearchCategory.Actors -> ActorSearchContent(state, contract)
    }
}

@Composable
private fun MovieSearchContent(state: SearchUiState, contract: SearchContract) {
    val moviesLazyList = state.moviesFlow.collectAsLazyPagingItems()

    MediaSearchContent(
        pagingItems = moviesLazyList,
        contract = contract,
        onNavigateToMovie = { id ->
            val movie = moviesLazyList.itemSnapshotList.items.firstOrNull { it.id == id }
            movie?.let {
                contract.addToRecentViewed(it.toRecentViewed())
                contract.onMovieGenreClick(it.genreIds)
            }
            contract.onMovieClick(id)
        }
    )
}

@Composable
private fun TvShowSearchContent(state: SearchUiState, contract: SearchContract) {
    val tvShowsLazyList = state.tvShowsFlow.collectAsLazyPagingItems()

    MediaSearchContent(
        pagingItems = tvShowsLazyList,
        contract = contract,
        onNavigateToTvShow = { id ->
            val tvShow = tvShowsLazyList.itemSnapshotList.items.firstOrNull { it.id == id }
            tvShow?.let {
                contract.addToRecentViewed(it.toRecentViewed())
                it.genres.forEach { genreId ->
                    contract.incrementGenreInterest(genreId, "tv")
                }
            }
            contract.onTvShowClick(id)
        }
    )
}

@Composable
private fun <T : Any> MediaSearchContent(
    pagingItems: LazyPagingItems<T>,
    contract: SearchContract,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {}
) {
    SearchContentWithErrorHandling(
        pagingItems,
        contract,
    ) { isLoading ->
        ResultOrEmpty(
            items = pagingItems.itemSnapshotList.items,
            emptyContent = {
                if (!isLoading) {
                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                }
            },
            content = {
                MediaLazyVerticalGrid(
                    pagingItems = pagingItems,
                    hasSaveIcon = true,
                    onSaveClick = { /* Handle save click */ },
                    isItemSaved = { false },
                    onNavigateToMovie = onNavigateToMovie,
                    onNavigateToTvShow = onNavigateToTvShow
                )
            }
        )
    }
}

@Composable
private fun ActorSearchContent(state: SearchUiState, contract: SearchContract) {
    val actorsLazyList =
        state.actorsFlow.collectAsLazyPagingItems()

    SearchContentWithErrorHandling(
        actorsLazyList,
        contract,
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
                        contract.onActorClick(it.id)
                    }
                )
            }
        )
    }
}

@Composable
fun RecentSearchLayOut(
    state: SearchUiState,
    contract: SearchContract,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val handleRecentSearchClick: (String) -> Unit = { query ->
        focusManager.clearFocus()
        keyboardController?.hide()
        contract.onRecentSearchClick(query)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.recentViewed.isNotEmpty()) {
            item {
                RecentViewedSection(
                    recentViewed = state.recentViewed,
                    onClearAll = contract::clearRecentViewed,
                    onNavigateToTvShowDetails = onNavigateToTvShowDetails,
                    onNavigateToMovieDetails = onNavigateToMovieDetails
                )
            }
        }

        if (state.recentSearches.isNotEmpty()) {
            item {
                RecentSearchesSection(
                    recentSearches = state.recentSearches,
                    onClearAll = contract::clearRecentSearches,
                    onSearchClick = handleRecentSearchClick,
                    onRemoveClick = contract::removeRecentSearch
                )
            }
        }
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
    contract: SearchContract,
    content: @Composable (Boolean) -> Unit
) {
    LaunchedEffect(lazyPagingItems.loadState) {
        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            contract.updateSearchState { copy(error = ErrorState.NoInternet) }
        }
    }

    val isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading
    val hasError = lazyPagingItems.loadState.refresh is LoadState.Error

    if (hasError) {
        NetworkErrorScreen(
            onRetry = { contract.onRetryClick() },
            onBack = null
        )
    } else {
        content(isLoading)
    }
}

//@ThemePreviews
//@Composable
//fun SearchScreenPreview() {
//    SearchScreen()
//}