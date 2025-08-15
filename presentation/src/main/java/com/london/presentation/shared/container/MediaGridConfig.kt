package com.london.presentation.shared.container

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

data class MediaGridConfig(
    val showSaveIcon: Boolean = true,
    val myRatingList: Boolean = false,
    val rate: String = "",
    val isDarkMode: Boolean = true,
    val isMovieSelected: Boolean = true,
    val isTvShowSelected: Boolean = false,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val onNavigateToMovie: (Int) -> Unit = {},
    val onNavigateToTvShow: (Int) -> Unit = {},
    val onSaveClick: (Any) -> Unit = {},
    val isItemSaved: (Any) -> Boolean = { false },
    val onDeleteClick: (Any) -> Unit = {}
)
