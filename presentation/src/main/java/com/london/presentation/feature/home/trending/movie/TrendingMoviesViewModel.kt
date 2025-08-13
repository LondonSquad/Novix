package com.london.presentation.feature.home.trending.movie

import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.feature.home.shared.handlingPagingFlow
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.utils.MovieGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        initializeMovies()
    }

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
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
        tryToCollect(
            block = {
                handlingPagingFlow { pageNumber ->
                    getTrendingMovies.invoke(
                        page = pageNumber,
                        movieGenreId = state.value.selectedGenreId
                    )
                }
            },
            onStart = { handlingLoadingState(true) },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onNewValue = { moviesFlow -> updateState { copy(moviesFlow = flowOf(moviesFlow)) } },
            onCompleted = { handlingLoadingState(false) },
        )
    }

    fun handlingLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}