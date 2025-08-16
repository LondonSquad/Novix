package com.london.presentation.feature.home.trending.movie

import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        reloadTrendingMovies()
    }

    override fun onGenreClick(genre: MovieGenreUi) {
        if (genre == state.value.selectedGenre) return
        updateState {
            copy(selectedGenre = genre)
        }
        reloadTrendingMovies()
    }

    override fun onBackClick() =
        emitEffect(TrendingMoviesEffect.NavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

    override fun onRetryClick() = reloadTrendingMovies()

    private fun reloadTrendingMovies() {
        tryToCollect(
            block = ::createTrendingMoviesPagingFlow,
            onStart = { handlingLoadingState(true) },
            onError = ::handlingErrorState,
            onNewValue = ::handlingPagingState,
        )
    }

    private fun handlingErrorState(errorState: ErrorState) =
        updateState { copy(errorState = errorState) }

    private fun handlingPagingState(moviesPagingData: PagingData<Trending>) {
        return updateState {
            copy(
                moviesFlow = flowOf(moviesPagingData),
                isLoading = false
            )
        }
    }

    private fun createTrendingMoviesPagingFlow() = createPagingSourceFlow(
        query = "",
        block = { _, pageNumber ->
            getMovieUseCase.getTrendingMovies(
                page = pageNumber,
                movieGenreId = state.value.selectedGenreId
            )
        }
    ).cachedIn(viewModelScope)

    private fun handlingLoadingState(isLoading: Boolean) =
        updateState { copy(isLoading = isLoading) }
}