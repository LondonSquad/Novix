package com.london.presentation.screen.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptySearchLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.Icon
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.R
import com.london.presentation.composables.ActorsLayout
import com.london.presentation.composables.MoviesLayOut
import com.london.presentation.composables.TriangleBlurredShape
import com.london.presentation.composables.TvShowLayOut
import com.london.presentation.composables.FilterBottomSheet
import com.london.presentation.composables.FilterState
import com.london.presentation.utils.Listen
import com.london.presentation.utils.ResultOrEmpty
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
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

    SearchScreenContent(
        state = state,
        interactionListener = viewModel,
        keyboardController = keyboardController,
        viewModel = viewModel,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                when (state.selectedCategory) {
                    SearchCategory.Movies -> {
                        val moviesLazyList = state.moviesFlow.collectAsLazyPagingItems()
                        val isLoading = moviesLazyList.loadState.refresh is LoadState.Loading
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
                            })
                    }

                    SearchCategory.TvShows -> {
                        val tvShowsLazyList = state.tvShowsFlow.collectAsLazyPagingItems()
                        val isLoading = tvShowsLazyList.loadState.refresh is LoadState.Loading
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
                                            viewModel.incrementGenreInterest(genreId, "tv")
                                        }
                                        viewModel.onTvShowClick(it.id)
                                    })
                            })
                    }

                    SearchCategory.Actors -> {
                        val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()
                        val isLoading = actorsLazyList.loadState.refresh is LoadState.Loading
                        ResultOrEmpty(
                            items = actorsLazyList.itemSnapshotList.items,
                            emptyContent = {
                                if (!isLoading) {
                                    NoSearchResultLayOut(modifier = Modifier.fillMaxSize())
                                }
                            },
                            content = {
                                ActorsLayout(
                                    actorsUis = actorsLazyList, onActorClick = {
                                        interactionListener.onActorClick(it.id)
                                    })
                            })
                    }
                }

            })
        }

        FilterBottomSheet(
            filterInteractions = viewModel,
            filterState = FilterState(
                isSheetVisible = state.showFilterBottomSheet,
                availableGenres = state.availableGenresWithNames,
                selectedGenres = state.selectedGenres,
                minimumImdbRating = state.imdbRating,
                releaseYearRange = state.releaseYearRange,
            ),
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
    AnimatedContent(
        targetState = uiState.showFilterButton, transitionSpec = {
            (fadeIn(animationSpec = tween(200)) + scaleIn(initialScale = 1f)) togetherWith
                    (fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 1f))
        }, modifier = modifier
    ) { showFilterButton ->
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    uiState.searchQuery.text.isNotEmpty() -> {
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

            if (showFilterButton) {
                PrimaryButton(
                    text = "",
                    onClick = viewModel::onFilterClick,
                    isLoading = false,
                    hasIcon = true,
                    icon = R.drawable.icon_filter,
                    hasLabel = false,
                    modifier = Modifier.width(52.dp)
                )
            }
        }
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
        horizontalArrangement = Arrangement.Start
    ) {
        NovixChip(
            text = stringResource(SearchCategory.Movies.title),
            isSelected = selected == SearchCategory.Movies,
            onClick = { onSelect(SearchCategory.Movies) })
        NovixChip(
            text = stringResource(SearchCategory.TvShows.title),
            isSelected = selected == SearchCategory.TvShows,
            onClick = { onSelect(SearchCategory.TvShows) })
        NovixChip(
            text = stringResource(SearchCategory.Actors.title),
            isSelected = selected == SearchCategory.Actors,
            onClick = { onSelect(SearchCategory.Actors) })
    }
}

@Composable
private fun RecentSearchLayOut(
    state: SearchUiState,
    interactionListener: SearchInteractions,
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
        RecentSearchesSection(
            recentSearches = state.recentSearches,
            onClearAll = interactionListener::clearRecentSearches,
            onSearchClick = interactionListener::onRecentSearchClick,
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
    EmptySearchLayout(
        text = stringResource(R.string.start_exploring_msg),
        image = R.drawable.imge_explore,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}


@Composable
private fun NoSearchResultLayOut(
    modifier: Modifier = Modifier
) {
    EmptySearchLayout(
        text = stringResource(R.string.no_search_result_msg),
        image = R.drawable.img_no_search_result,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@ThemePreviews
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}