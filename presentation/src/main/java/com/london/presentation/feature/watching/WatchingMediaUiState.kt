package com.london.presentation.feature.watching

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

data class WatchingMediaUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val movies: List<Movie> = emptyList(),
    val tvSeries: List<TvShow> = emptyList(),
    val tabSelected: Int = 0,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val isMovieSelected: Boolean = true,
    val isTvSelected: Boolean = true
)