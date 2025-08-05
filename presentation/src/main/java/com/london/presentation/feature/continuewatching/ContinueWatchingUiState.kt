package com.london.presentation.feature.continuewatching

import com.london.designsystem.component.MediaCategory
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ContinueWatchingUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val movies: Flow<List<Movie>> = emptyFlow(),
    val tvSeries: Flow<List<TvShow>> = emptyFlow(),
    val selectedMediaCategory: MediaCategory = MediaCategory.MOVIES,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val isMovieSelected: Boolean = true,
    val isTvSelected: Boolean = true
)
