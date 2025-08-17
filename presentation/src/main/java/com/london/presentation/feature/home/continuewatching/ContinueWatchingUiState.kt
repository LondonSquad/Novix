package com.london.presentation.feature.home.continuewatching

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ContinueWatchingUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val movies: Flow<List<Movie>> = emptyFlow(),
    val tvSeries: Flow<List<TvShow>> = emptyFlow(),
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val selectedTvShowGenre: TvShowGenreUi = TvShowGenreUi.All,
    val selectedMediaCategory: MediaCategory = MediaCategory.Movies
)
