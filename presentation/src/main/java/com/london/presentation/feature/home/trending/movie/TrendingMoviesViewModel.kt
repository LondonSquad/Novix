package com.london.presentation.feature.home.trending.movie

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Trending
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        getTrendingMovies()
    }

    override fun onGenreClick(genre: MovieGenreUi) {
        if (genre == state.value.selectedGenre) return
        updateState {
            copy(selectedGenre = genre)
        }
        getTrendingMovies()
    }

    override fun onBackClick() =
        emitEffect(TrendingMoviesEffect.OnNavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(TrendingMoviesEffect.OnNavigateToMovieClick(id))

    override fun onRetryClick() = getTrendingMovies()

    private fun getTrendingMovies() {
        tryToCollect(
            block = ::createTrendingMoviesPagingFlow,
            onStart = { setLoadingState(true) },
            onError = ::setErrorState,
            onNewValue = ::setPagingState,
        )
    }

    private fun setErrorState(errorState: ErrorState) =
        updateState { copy(errorState = errorState) }

    private fun setPagingState(moviesPagingData: PagingData<Trending>) {
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
                movieGenre = state.value.selectedGenre.toDomain()
            )
        }
    ).cachedIn(viewModelScope)

    private fun setLoadingState(isLoading: Boolean) =
        updateState { copy(isLoading = isLoading) }
}