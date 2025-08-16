package com.london.presentation.shared.container

import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

data class MediaGridConfig(
    val showSaveIcon: Boolean = true,
    val myRatingList: Boolean = false,
    val rate: String = "3",
    val isDarkMode: Boolean = true,
    val isMovieSelected: Boolean = true,
    val isTvShowSelected: Boolean = false,
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val selectedTvShowGenre: TvShowGenreUi = TvShowGenreUi.All,
    val onNavigateToMovie: (Int) -> Unit = {},
    val onNavigateToTvShow: (Int) -> Unit = {},
    val onSaveClick: (Any) -> Unit = {},
    val isItemSaved: (Any) -> Boolean = { false },
    val onDeleteClick: (Any) -> Unit = {}
)
