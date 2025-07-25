package com.london.presentation.screen.toprated

import com.london.domain.usecase.GetTopRatedMoviesUseCase
import com.london.domain.usecase.GetTopRatedTvSeriesUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopRatedViewModel(
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
                getTopRatedMoviesUseCase(
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
                getTopRatedTvSeriesUseCase(
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

    override fun movieGenreClicked(genre: MovieGenre) {
        updateState { copy(selectedMovieGenre = genre) }
        initializeTopMovies()
    }

    override fun tvShowGenreClicked(genre: TvShowGenre) {
        updateState { copy(selectedTvShowGenre = genre) }
        initializeTvShow()
    }

    override fun tabSelected(index: Int) {
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

