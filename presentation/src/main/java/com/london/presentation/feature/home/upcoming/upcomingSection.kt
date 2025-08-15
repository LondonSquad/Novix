package com.london.presentation.feature.home.upcoming

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.shimmerEffect
import com.london.domain.entity.UpComingMovie
import com.london.presentation.R
import com.london.presentation.feature.home.HomeScreenContract
import com.london.presentation.feature.home.HomeScreenUiState
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.HomeCard

fun LazyGridScope.upcomingSection(
    contract: HomeScreenContract,
    isHeaderStuck: Boolean = false,
    screenWidth: Dp,
    state: HomeScreenUiState,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    isLoading: Boolean = false
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        UpcomingSectionTitle(isLoading = isLoading)
    }

    stickyHeader {
        UpcomingStickyHeader(
            isLoading = isLoading,
            isHeaderStuck = isHeaderStuck,
            screenWidth = screenWidth,
            state = state,
            contract = contract
        )
    }

    items(count = upcomingMoviesLazyList.itemCount) { index ->
        val movie = upcomingMoviesLazyList[index]

        UpcomingMovieItem(
            movie = movie,
            isLoading = isLoading,
            onMovieClick = { contract.onMovieClick(movie?.id ?: 0) }
        )
    }
}

@Composable
private fun UpcomingSectionTitle(isLoading: Boolean) {
    if (!isLoading) {
        Text(
            text = stringResource(R.string.upcoming),
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
        )
    } else {
        Box(
            modifier = Modifier
                .height(20.dp)
                .padding(bottom = 4.dp)
                .wrapContentWidth()
                .shimmerEffect()
        )
    }
}

@Composable
private fun UpcomingStickyHeader(
    isLoading: Boolean,
    isHeaderStuck: Boolean,
    screenWidth: Dp,
    state: HomeScreenUiState,
    contract: HomeScreenContract
) {
    val animatedPadding by animateDpAsState(
        targetValue = if (isHeaderStuck) 8.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "header_padding"
    )

    GenresSection(
        isLoading = isLoading,
        genres = state.movieGenres,
        selectedGenreId = state.selectedMovieGenre.id,
        screenWidth = screenWidth,
        onGenreClick = contract::onMovieGenreSelect,
        modifier = Modifier
            .background(NovixTheme.colors.surface)
            .padding(bottom = animatedPadding),
        getGenreId = { it.id },
        getGenreName = { stringResource(it.stringResId) }
    )
}

@Composable
private fun UpcomingMovieItem(
    movie: UpComingMovie?,
    isLoading: Boolean,
    onMovieClick: () -> Unit
) {
    when {
        isLoading || movie == null -> {
            ShimmerMovieCard()
        }
        else -> {
            HomeCard(
                imageUrl = movie.imageUrl,
                isSaved = false,
                onSaveClick = { /* TODO */ },
                modifier = Modifier
                    .clipToBounds()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMovieClick() }
            )
        }
    }
}

@Composable
private fun ShimmerMovieCard() {
    Box(
        modifier = Modifier
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
    )
}