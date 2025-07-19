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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptySearchLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.R
import com.london.presentation.composables.ActorsLayout
import com.london.presentation.composables.MoviesLayOut
import com.london.presentation.composables.TriangleBlurredShape
import com.london.presentation.composables.TvShowLayOut
import com.london.presentation.composables.filterbottomsheet.FilterBottomSheet
import com.london.presentation.utils.ResultOrEmpty
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToActorDetails: (Int) -> Unit = { },
    onNavigateToTvShowDetails: (Int) -> Unit = { },
    onNavigateToMovieDetails: (Int) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreenContent(
        state = state,
        interactionListener = viewModel,
        keyboardController = keyboardController,
        viewModel = viewModel,
        onNavigateToTvShowDetails = onNavigateToTvShowDetails,
        onNavigateToActorDetails = onNavigateToActorDetails,
        onNavigateToMovieDetails = onNavigateToMovieDetails
    )
}

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    interactionListener: SearchInteractions,
    viewModel: SearchViewModel,
    keyboardController: SoftwareKeyboardController?,
    onNavigateToActorDetails: (Int) -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {

        TriangleBlurredShape()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NovixTheme.colors.surface)
                , verticalArrangement = Arrangement.Top
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
                onFilterClick = { showFilterBottomSheet = true },
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
                            onNavigateToTvShowDetails = onNavigateToTvShowDetails,
                            onNavigateToMovieDetails = onNavigateToMovieDetails
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
                        ResultOrEmpty(
                            items = moviesLazyList.itemSnapshotList.items,
                            emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
                            content = {
                                MoviesLayOut(
                                    movieUis = moviesLazyList,
                                    onSaveClick = { /* Handle save click */ },
                                    isMovieSaved = { false },
                                    onMovieClick = {
                                        viewModel.addToRecentViewed(it.toRecentViewed())
                                        viewModel.onClickMovie(it.genreIds)
                                        onNavigateToMovieDetails(it.id)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            })
                    }

                    SearchCategory.TvShows -> {
                        val tvShowsLazyList = state.tvShowsFlow.collectAsLazyPagingItems()
                        ResultOrEmpty(
                            items = tvShowsLazyList.itemSnapshotList.items,
                            emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
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
                                        onNavigateToTvShowDetails(it.id)
                                    })
                            })
                    }

                    SearchCategory.Actors -> {
                        val actorsLazyList = state.actorsFlow.collectAsLazyPagingItems()
                        ResultOrEmpty(
                            items = actorsLazyList.itemSnapshotList.items,
                            emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
                            content = {
                                ActorsLayout(
                                    actorsUis = actorsLazyList, onActorClick = {
                                        onNavigateToActorDetails(it.id)
                                    })
                            })
                    }
                }

            })
        }
        if (showFilterBottomSheet) {
            FilterBottomSheet(
                onDismissRequest = { showFilterBottomSheet = false })
        }
    }
}

@Composable
private fun SearchBar(
    uiState: SearchUiState,
    viewModel: SearchViewModel,
    interactionSource: MutableInteractionSource,
    keyboardController: SoftwareKeyboardController?,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = uiState.showFilterButton, transitionSpec = {
            (fadeIn(animationSpec = tween(0)) + scaleIn(initialScale = 0.98f)) togetherWith (fadeOut(
                animationSpec = tween(0)
            ) + scaleOut(targetScale = 0.98f))
        }, modifier = modifier
    ) { showFilterButton ->
        Row(
            modifier = Modifier.fillMaxWidth()
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
                        viewModel.addToRecentSearches(uiState.searchQuery.text)
                    }),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f)
            )

            if (showFilterButton) {
                Spacer(modifier = Modifier.width(8.dp))
                PrimaryButton(
                    text = "",
                    onClick = onFilterClick,
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
                modifier = Modifier.clickable{
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
    recentSearches: List<String>,
    onClearAll: () -> Unit,
    onSearchClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit
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
                search = search,
                onSearchClick = { onSearchClick(search) },
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
    Row(modifier = modifier
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
        HorizontalDivider(
            color = NovixTheme.colors.stroke,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 7.5.dp)
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