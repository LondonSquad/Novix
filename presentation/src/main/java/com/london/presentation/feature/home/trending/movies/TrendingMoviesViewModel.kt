package com.london.presentation.feature.home.trending.movies

import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        initializeMovies()
    }

    private fun initializeMovies() {
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getTrendingMovies.invoke(page = pageNumber)
                    val filteredItems = if (state.value.selectedGenreId != null && state.value.selectedGenreId != -1) {
                        movies.items.filter { it.genreIds.contains(state.value.selectedGenreId) }
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

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState {
            copy(selectedGenreId = genre.id)
        }
        initializeMovies()
    }

    override fun onBack() = emitEffect(TrendingMoviesEffect.NavigateBack)

    override fun onMovieClick(id: Int) = emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

}