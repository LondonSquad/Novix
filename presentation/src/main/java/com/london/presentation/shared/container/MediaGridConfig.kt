package com.london.presentation.shared.container

import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

data class MediaGridConfig(
    val showSaveIcon: Boolean = true,
    val isDarkMode: Boolean = true,
    val isMovieSelected: Boolean = true,
    val isTvShowSelected: Boolean = false,
    val isItemSaved: (Any) -> Boolean = { false },
    val onSaveClick: (Any) -> Unit = {},
    val onDeleteClick: (Any) -> Unit = {},
    val onNavigateToMovie: (Int) -> Unit = {},
    val onNavigateToTvShow: (Int) -> Unit = {},
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val selectedTvShowGenre: TvShowGenreUi = TvShowGenreUi.All
)
