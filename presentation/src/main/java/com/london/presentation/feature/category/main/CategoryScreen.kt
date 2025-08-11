package com.london.presentation.feature.category.main

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TopBar
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.shared.CategoriesItem
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier
) {

    Column(
        Modifier
            .statusBarsPadding()
            .padding(top = 12.dp)
    ) {
        TopBar(
            title = stringResource(R.string.categories),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        CategoriesSelection(
            onClick = {}
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            movieGenres(
                genres = MovieGenre.entries.filter { it != MovieGenre.All },
                onClick = {},
            )
            tvShowGenres(
                genres = TvShowGenre.entries.filter { it != TvShowGenre.All },
                onClick = {},
            )
        }
    }
}

@Composable
private fun CategoriesSelection(
    onClick: (MediaCategory) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(MediaCategory.entries) {
            NovixChip(
                text = stringResource(it.title),
                onClick = { onClick(it) },
                isSelected = true
            )
        }
    }
}

private fun LazyGridScope.movieGenres(
    genres: List<MovieGenre>,
    onClick: (MovieGenre) -> Unit,
) {
    items(genres) {
        CategoriesItem(
            categoryName = listOf(it.stringResId.string),
            categoryImage = it.backgroundResId,
            onClick = { onClick(it) },
        )
    }
}

private fun LazyGridScope.tvShowGenres(
    genres: List<TvShowGenre>,
    onClick: (TvShowGenre) -> Unit,
) {
    items(genres) {
        CategoriesItem(
            categoryName = stringResource(it.stringResId).split('&'),
            categoryImage = it.backgroundResId,
            onClick = { onClick(it) },
        )
    }
}
