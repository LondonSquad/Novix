package com.london.presentation.feature.continuewatching

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

data class ContinueWatchingUiState(
    val movies: List<Movie> = emptyList(),
    val tvSeries: List<TvShow> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val tabSelected: Int = 0,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val isMovieSelected: Boolean = true
)
