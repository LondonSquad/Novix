package com.london.presentation.feature.home.trending.movie

import androidx.paging.PagingData
import com.london.domain.entity.Trending
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TrendingMoviesUiState(
    val isSaved: Boolean = false,
    val selectedGenre: MovieGenreUi? = MovieGenreUi.All,
    val backdropPath: String = "",
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val moviesFlow: Flow<PagingData<Trending>> = emptyFlow(),
    val movieGenres: List<MovieGenreUi> = MovieGenreUi.getList()
)
