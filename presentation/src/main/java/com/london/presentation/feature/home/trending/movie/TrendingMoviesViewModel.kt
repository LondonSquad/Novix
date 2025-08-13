package com.london.presentation.feature.home.trending.movie

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Trending
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
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
        reloadTrendingMovies()
    }

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState {
            copy(selectedGenreId = genre.id)
        }
        reloadTrendingMovies()
    }

    override fun onBack() =
        emitEffect(TrendingMoviesEffect.NavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

    override fun onRetry() {
        reloadTrendingMovies()
    }

    private fun reloadTrendingMovies() {
        tryToCollect(
            block = ::createTrendingMoviesPagingFlow,
            onStart = { handlingLoadingState(true) },
            onError = ::handlingErrorState,
            onNewValue = ::handlingPagingState,
            onCompleted = { handlingLoadingState(false) },
        )
    }

    fun handlingErrorState(errorState: ErrorState) = updateState { copy(errorState = errorState) }
    fun handlingPagingState(moviesPagingData: PagingData<Trending>) {
        return updateState {
            copy(
                moviesFlow = flowOf(moviesPagingData)
            )
        }
    }

    fun createTrendingMoviesPagingFlow() = createPagingSourceFlow(
        query = "",
        block = { _, pageNumber ->
            getTrendingMovies.invoke(
                page = pageNumber,
                movieGenreId = state.value.selectedGenreId
            )
        }
    ).cachedIn(viewModelScope)

    fun handlingLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}