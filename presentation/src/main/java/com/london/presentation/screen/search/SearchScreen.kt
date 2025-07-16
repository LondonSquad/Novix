package com.london.presentation.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.EmptySearchLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.composables.ActorsLayout
import com.london.presentation.composables.MoviesLayOut
import com.london.presentation.composables.TvShowLayOut
import com.london.presentation.composables.filterbottomsheet.FilterBottomSheet
import com.london.presentation.utils.ResultOrEmpty
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToTvShowDetails: (Int) -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreenContent(
        state = state,
        interactionListener = viewModel,
        keyboardController = keyboardController,
        viewModel = viewModel,
        onNavigateToTvShowDetails = onNavigateToTvShowDetails
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreenContent(
    state: SearchUiState,
    interactionListener: SearchInteractions,
    viewModel: SearchViewModel,
    keyboardController: SoftwareKeyboardController?,
    onNavigateToTvShowDetails: (Int) -> Unit
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
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
        )
        {
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

            ResultOrEmpty(
                items = state.searchQuery.text.toList(),
                emptyContent = {
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
                                viewModel = viewModel
                            )
                        }
                    )
                },
                content = {
                    SearchChipsRow(
                        selected = state.selectedCategory,
                        onSelect = interactionListener::onCategorySelected,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    when (state.selectedCategory) {
                        SearchCategory.Movies -> {
                            ResultOrEmpty(
                                items = state.movieResults,
                                emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
                                content = {
                                    MoviesLayOut(
                                        movieUis = state.movieResults,
                                        onSaveClick = { /* Handle save click */ },
                                        isMovieSaved = { false },
                                        onMovieClick = {
                                            viewModel.addToRecentViewed(it.posterPicture)
                                            viewModel.onClickMovie(it.id)
                                        },
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            )
                        }

                        SearchCategory.TvShows -> ResultOrEmpty(
                            items = state.tvShowUiResults,
                            emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
                            content = {
                                TvShowLayOut(
                                    tvShowUis = state.tvShowUiResults,
                                    onSaveClick = { /* Handle save click */ },
                                    isTvShowSaved = { false },
                                    onTvShowClick = {
                                        viewModel.addToRecentViewed(it.posterPicture)
                                        viewModel.incrementGenreInterest(it.id, "tv")
                                        onNavigateToTvShowDetails(it.id)
                                    }
                                )
                            }
                        )

                        SearchCategory.Actors -> ResultOrEmpty(
                            items = state.actorUiResults,
                            emptyContent = { NoSearchResultLayOut(modifier = Modifier.fillMaxSize()) },
                            content = {
                                ActorsLayout(
                                    actorsUis = state.actorUiResults,
                                    onActorClick = { /* Handle actor click */ }
                                )
                            }
                        )
                    }
                }
            )
        }
        if (showFilterBottomSheet) {
            FilterBottomSheet(
                onDismissRequest = { showFilterBottomSheet = false }
            )
        }
    }
}

@Composable
private fun TriangleBlurredShape() {
    val triangleBackgroundColor: Color = NovixTheme.colors.primary.copy(alpha = 0.08f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(150.dp)
            .drawWithContent {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.7f, 0f)
                    lineTo(0f, size.height * 0.25f)
                    close()
                }
                drawPath(
                    path,
                    color = triangleBackgroundColor,
                )
                drawContent()
            }
    )
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
        targetState = uiState.selectedCategory,
        transitionSpec = {
            fadeIn(animationSpec = tween(200)) togetherWith
                    fadeOut(animationSpec = tween(200)) using
                    SizeTransform(clip = false)
        },
        modifier = modifier
    ) { category ->
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
                modifier = Modifier.weight(1f)
            )

            if (category != SearchCategory.Actors) {
                Spacer(modifier = Modifier.width(8.dp))
                PrimaryButton(
                    text = "",
                    onClick = onFilterClick,
                    isLoading = false,
                    isDisabled = false,
                    hasIcon = true,
                    icon = R.drawable.icon_filter,
                    hasLabel = false,
                    modifier = Modifier
                        .width(52.dp)
                )
            }
        }
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
        horizontalArrangement = Arrangement.Start
    ) {
        NovixChip(
            text = stringResource(SearchCategory.Movies.title),
            isSelected = selected == SearchCategory.Movies,
            onClick = { onSelect(SearchCategory.Movies) }
        )
        NovixChip(
            text = stringResource(SearchCategory.TvShows.title),
            isSelected = selected == SearchCategory.TvShows,
            onClick = { onSelect(SearchCategory.TvShows) }
        )
        NovixChip(
            text = stringResource(SearchCategory.Actors.title),
            isSelected = selected == SearchCategory.Actors,
            onClick = { onSelect(SearchCategory.Actors) }
        )
    }
}

@Composable
private fun RecentSearchLayOut(
    state: SearchUiState,
    interactionListener: SearchInteractions,
    viewModel: SearchViewModel
) {
    AnimatedVisibility(
        visible = state.recentViewed.isNotEmpty(),
        exit = fadeOut()
    ) {
        Column {
            RecentViewedSection(
                recentViewed = state.recentViewed,
                onClearAll = { viewModel.clearRecentViewed() }
            )
        }
    }

    AnimatedVisibility(
        visible = state.recentSearches.isNotEmpty(),
        exit = fadeOut()
    ) {
        Column {
            RecentSearchesSection(
                recentSearches = state.recentSearches,
                onClearAll = interactionListener::clearRecentSearches,
                onSearchClick = interactionListener::onRecentSearchClick,
                onRemoveClick = interactionListener::removeRecentSearch
            )
        }
    }
}


@Composable
fun RecentViewedSection(
    recentViewed: List<String>,
    onClearAll: () -> Unit
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
        items(recentViewed) { imageUrl ->
            HomeCard(
                imageUrl = imageUrl,
                isSaved = false,
                onSaveClick = {}
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
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
                .clickable { onRemoveClick() }
        )
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
        modifier = modifier
            .padding(horizontal = 16.dp)
    )
}

@ThemePreviews
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}