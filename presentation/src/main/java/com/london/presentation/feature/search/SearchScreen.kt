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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.entity.shared.MediaType
import com.london.presentation.R
import com.london.presentation.shared.ActorsLayout
import com.london.presentation.shared.HomeCard
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
            is SearchEffect.ActorDetailsNavigation -> onNavigateToActorDetails(currentEffect.actorId)
            is SearchEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(currentEffect.movieId)
            is SearchEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(currentEffect.tvId)
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
        onRetry = contract::onRetryClick,
        pagingFlow = currentPagingFlow,
        handlePagingLoadingAutomatically = false
    ) {
        SearchMainContent(
            state = state,
            contract = contract,
            interactionSource = interactionSource,
            keyboardController = keyboardController,
            onClearFocus = { focusManager.clearFocus() }
        )
    }
}

@Composable
private fun SearchMainContent(
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
    ) {
        TriangleBlurredShape()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NovixTheme.colors.surface),
            verticalArrangement = Arrangement.Top
        ) {
            TopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
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

            SearchBody(state = state, contract = contract)
        }
    }
}

@Composable
private fun SearchBody(
    state: SearchUiState,
    contract: SearchContract
) {
    if (state.error != null && state.error != ErrorState.NoInternet) {
        ResultOrEmpty(
            items = state.searchQuery.text.toList(),
            emptyContent = { RecentSection(state = state, contract = contract) },
            content = {
                SearchChipsRow(
                    selected = state.selectedCategory,
                    onSelect = contract::onCategorySelected,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        )
    } else {
        ResultOrEmpty(
            items = state.searchQuery.text.toList(),
            emptyContent = {
                RecentSection(
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
        when (state.selectedCategory) {
            SearchCategory.Movies -> MovieSearchContent(state, contract)
            SearchCategory.TvShows -> TvShowSearchContent(state, contract)
            SearchCategory.Actors -> ActorSearchContent(state, contract)
        }
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
                contract.onMovieGenreClick(it.genres)
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
                it.genres.forEach { genre ->
                    contract.incrementGenreInterest(genre, "tv")
                }
            }
            contract.onTvShowClick(id)
        }
    )
}

@Composable
private fun ActorSearchContent(state: SearchUiState, contract: SearchContract) {
    val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()

    EmptyContent(
        actorsLazyList,
        contract,
    ) { isLoading ->
        ResultOrEmpty(
            items = actorsLazyList.itemSnapshotList.items,
            emptyContent = {
                if (!isLoading) {
                    EmptyLayout(
                        text = stringResource(R.string.no_search_result_msg),
                        image = R.drawable.img_no_search_result,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    )
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
private fun <T : Any> MediaSearchContent(
    pagingItems: LazyPagingItems<T>,
    contract: SearchContract,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {}
) {
    EmptyContent(
        pagingItems,
        contract,
    ) { isLoading ->
        ResultOrEmpty(
            items = pagingItems.itemSnapshotList.items,
            emptyContent = {
                if (!isLoading) {
                    EmptyLayout(
                        text = stringResource(R.string.no_search_result_msg),
                        image = R.drawable.img_no_search_result,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    )
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
private fun EmptyContent(
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

@Composable
private fun SearchBar(
    uiState: SearchUiState,
    contract: SearchContract,
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
            onValueChange = { contract.onSearchQueryChange(it) },
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    style = NovixTheme.typography.body.small,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            leadingIcon = painterResource(id = R.drawable.icon_search_normal),
            trailingIcon = {
                TrailingClearIcon(
                    isVisible = uiState.searchQuery.text.isNotEmpty() && focusedState,
                    onClear = contract::clearSearch
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = onSearchKeyboardAction(
                focusManager = focusManager,
                keyboardController = keyboardController,
                query = uiState.searchQuery.text,
                onAddRecent = contract::addToRecentSearches
            ),
            interactionSource = interactionSource,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TrailingClearIcon(
    isVisible: Boolean,
    onClear: () -> Unit
) {
    if (!isVisible) return
    Icon(
        painter = painterResource(id = R.drawable.icon_remove_filled),
        contentDescription = stringResource(R.string.clear),
        tint = NovixTheme.colors.hint,
        modifier = Modifier
            .size(20.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClear() }
    )
}

private fun onSearchKeyboardAction(
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    query: String,
    onAddRecent: (String) -> Unit
): KeyboardActions {
    return KeyboardActions(
        onSearch = {
            focusManager.clearFocus()
            keyboardController?.hide()
            onAddRecent(query)
        }
    )
}

@Composable
private fun RecentSection(
    state: SearchUiState,
    contract: SearchContract,
    modifier: Modifier = Modifier
) {
    ResultOrEmpty(
        items = state.recentSearches,
        otherItems = state.recentViewed,
        emptyContent = {
            EmptyLayout(
                text = stringResource(R.string.start_exploring_msg),
                image = R.drawable.imge_explore,
                modifier = modifier.padding(horizontal = 16.dp)
            )
        },
        content = {
            RecentSectionContent(
                state = state,
                contract = contract,
                onNavigateToTvShowDetails = contract::onTvShowClick,
                onNavigateToMovieDetails = contract::onMovieClick
            )
        }
    )
}

@Composable
fun RecentSectionContent(
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
                RecentSearchSection(
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
private fun RecentViewedSection(
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
private fun RecentSearchSection(
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

    Column(
        modifier = Modifier
            .background(NovixTheme.colors.surface)
            .padding(horizontal = 16.dp)
    ) {
        val lastIndex = recentSearches.lastIndex
        recentSearches.forEachIndexed { index, search ->
            RecentSearchItem(
                search = search.query,
                onSearchClick = { onSearchClick(search.query) },
                onRemoveClick = { onRemoveClick(search) },
                showDivider = index != lastIndex
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
        RecentSearchSeparator()
    }
}

@Composable
private fun SearchChipsRow(
    selected: SearchCategory,
    onSelect: (SearchCategory) -> Unit,
    modifier: Modifier = Modifier
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
private fun RecentSearchSeparator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.5.dp)
            .height(1.dp)
            .background(NovixTheme.colors.stroke)
    )
}

@ThemePreviews
@Composable
private fun Preview() {
    SearchScreen(
        onNavigateToActorDetails = {},
        onNavigateToTvShowDetails = {},
        onNavigateToMovieDetails = {}
    )
}