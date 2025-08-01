package com.london.presentation.feature.toprated

import com.london.domain.usecase.toprated.GetTopRatedMoviesUseCase
import com.london.domain.usecase.toprated.GetTopRatedTvSeriesUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase,
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState()), TopRatedContract {

    init {
        initializeTopRated()
    }

    private fun initializeTopRated() {
        if (state.value.isMovieSelected) initializeTopMovies()
        else initializeTvShow()
    }

    private fun initializeTopMovies() {
        tryToExecute(block = {
            val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                getTopRatedMoviesUseCase.invoke(
                    pageNumber,
                    if (state.value.selectedMovieGenre == MovieGenre.All) null
                    else state.value.selectedMovieGenre.id
                )
            }

            moviesFlow
        }, onStart = {
            updateState { copy(isLoading = true) }
        }, onSuccess = { media ->
            updateState { copy(movies = media) }
        }, onError = { error ->
            updateState { copy(errorMessage = error.toString()) }
        }, onCompleted = {
            updateState { copy(isLoading = false) }
        }, checkSuccess = { true })
    }

    private fun initializeTvShow() {
        tryToExecute(block = {
            val tvSeriesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                getTopRatedTvSeriesUseCase.invoke(
                    pageNumber,
                    if (state.value.selectedTvShowGenre == TvShowGenre.All) null
                    else state.value.selectedTvShowGenre.id
                )
            }
            tvSeriesFlow
        }, onStart = {
            updateState { copy(isLoading = true) }
        }, onSuccess = { media ->
            updateState { copy(tvSeries = media) }
        }, onError = { error ->
            updateState { copy(errorMessage = error.toString()) }
        }, onCompleted = {
            updateState { copy(isLoading = false) }
        }, checkSuccess = { true })
    }

    override fun onRetry(){
        updateState { copy(errorMessage = null) }
        initializeTopRated()
    }

    override fun movieGenre(genre: MovieGenre) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        initializeTopMovies()
    }

    override fun tvShowGenre(genre: TvShowGenre) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        initializeTvShow()
    }

    override fun tabSelected(index: Int) {
        if (index == state.value.tabSelected) return
        updateState {
            copy(
                tabSelected = index, isMovieSelected = index == 0
            )
        }
        initializeTopRated()
    }

    override fun onBackClicked() {
        emitEffect(TopRatedEffect.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
      emitEffect(TopRatedEffect.NavigateToMovieDetails(id))
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(TopRatedEffect.NavigateToTvShowDetails(id))
    }
}