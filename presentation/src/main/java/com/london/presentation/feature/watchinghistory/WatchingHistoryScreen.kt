package com.london.presentation.feature.watchinghistory

import androidx.compose.runtime.Composable
import com.london.presentation.R
import com.london.presentation.feature.watching.WatchingMediaScreen

@Composable
fun WatchingHistoryScreen(
    onBackClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    onTvShowClick: (Int) -> Unit = {},
) {
    WatchingMediaScreen(
        screenTitle = R.string.watching_history,
        onBackClick = onBackClick,
        onMovieClick = onMovieClick,
        onTvShowClick = onTvShowClick
    )
}
