package com.london.presentation.feature.home.trending.movie

import com.london.domain.entity.genre.MovieGenre
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        initializeMovies()
    }

    override fun onGenreSelected(genre: MovieGenreUi) {
        if (genre == state.value.selectedGenre) return
        updateState {
            copy(selectedGenre = genre)
        }
        initializeMovies()
    }

    override fun onBack() =
        emitEffect(TrendingMoviesEffect.NavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

    override fun onRetry() {
        initializeMovies()
    }

    private fun initializeMovies() {
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getMovieUseCase.getTrendingMovies(page = pageNumber)
                    val filteredItems =
                        if (state.value.selectedGenre != null && state.value.selectedGenre != MovieGenreUi.All) {
                            movies.items.filter { movie ->
                                movie.genres.map { (it as MovieGenre).toUi() }
                                    .contains(state.value.selectedGenre)
                            }
                        } else {
                            movies.items
                        }
                    movies.copy(items = filteredItems)
                }
                moviesFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(moviesFlow = moviesFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }
}