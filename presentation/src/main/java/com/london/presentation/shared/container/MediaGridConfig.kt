package com.london.presentation.shared.container

import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

data class MediaGridConfig(
    val rate: String? = "",
    val isDarkMode: Boolean = true,
    val showSaveIcon: Boolean = true,
    val isMovieSelected: Boolean = true,
    val onSaveClick: (Any) -> Unit = {},
    val onDeleteClick: (Any) -> Unit = {},
    val isTvShowSelected: Boolean = false,
    val onNavigateToMovie: (Int) -> Unit = {},
    val onNavigateToTvShow: (Int) -> Unit = {},
    val isItemSaved: (Any) -> Boolean = { false },
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val selectedTvShowGenre: TvShowGenreUi = TvShowGenreUi.All,
)
