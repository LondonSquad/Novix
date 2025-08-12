package com.london.presentation.feature.category.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.shared.CategoriesItem
import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.gridColumns

@Composable
fun CategoriesScreen(
    onNavigateToMovieCategory: (MovieGenre) -> Unit,
    onNavigateToTvShowCategory: (TvShowGenre) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is CategoriesEffect.NavigateToMovieCategory ->
                onNavigateToMovieCategory(currentEffect.movieGenre)

            is CategoriesEffect.NavigateToTvShowCategory ->
                onNavigateToTvShowCategory(currentEffect.tvShowGenre)
        }
    }

    Content(state = state, contract = viewModel)
}

@Composable
private fun Content(
    state: CategoriesUiState,
    contract: CategoriesContract,
) {
    Column(
        Modifier
            .statusBarsPadding()
            .padding(top = 12.dp)
    ) {
        TopBar(
            title = stringResource(R.string.categories),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        CategoriesSelection(
            onClick = contract::onCategoryClick,
            selectedCategory = state.selectedCategory,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        GenresGrid(
            state = state,
            contract = contract,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun GenresGrid(
    state: CategoriesUiState,
    contract: CategoriesContract,
    modifier: Modifier = Modifier
) {

    AnimatedContent(
        targetState = state.selectedCategory,
        transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) }
    ) { category ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns(itemWidth = 160)),
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp, top = 12.dp),
        ) {
            when (category) {
                MediaCategory.Movies -> movieGenres(
                    genres = state.movieGenres,
                    onClick = contract::onMovieGenreClick,
                )

                else -> tvShowGenres(
                    genres = state.tvShowGenres,
                    onClick = contract::onTvShowGenreClick,
                )
            }
        }
    }
}

@Composable
private fun CategoriesSelection(
    selectedCategory: MediaCategory,
    onClick: (MediaCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(MediaCategory.entries) {
            NovixChip(
                text = stringResource(it.tabTextResId),
                onClick = { onClick(it) },
                isSelected = it == selectedCategory,
            )
        }
    }
}

private fun LazyGridScope.movieGenres(
    onClick: (MovieGenre) -> Unit,
    genres: List<MovieGenre>,
) {
    items(genres) {
        CategoriesItem(
            categoryName = it.stringResId.string,
            categoryImage = it.backgroundResId,
            onClick = { onClick(it) },
            modifier = Modifier.animateItem()
        )
    }
}

private fun LazyGridScope.tvShowGenres(
    onClick: (TvShowGenre) -> Unit,
    genres: List<TvShowGenre>,
) {
    items(genres) {
        CategoriesItem(
            categoryName = stringResource(it.stringResId),
            categoryImage = it.backgroundResId,
            onClick = { onClick(it) },
            modifier = Modifier.animateItem()
        )
    }
}

@ThemePreviews
@Composable
private fun Preview() {
    Content(
        state = CategoriesUiState(
            selectedCategory = MediaCategory.Movies,
            movieGenres = MovieGenre.entries.filter { it != MovieGenre.All },
            tvShowGenres = TvShowGenre.entries.filter { it != TvShowGenre.All },
        ),
        contract = object : CategoriesContract {
            override fun onMovieGenreClick(genre: MovieGenre) {}
            override fun onTvShowGenreClick(genre: TvShowGenre) {}
            override fun onCategoryClick(category: MediaCategory) {}
        },
    )
}
