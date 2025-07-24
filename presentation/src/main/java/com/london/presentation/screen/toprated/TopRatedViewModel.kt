package com.london.presentation.screen.toprated

import com.london.domain.usecase.GetTopRatedMoviesUseCase
import com.london.domain.usecase.GetTopRatedTvSeriesUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
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
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    getTopRatedMoviesUseCase(pageNumber)
                }

                val tvSeriesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    getTopRatedTvSeriesUseCase(pageNumber)
                }

                MediaUiState.Combined(
                    movies = moviesFlow,
                    tvSeries = tvSeriesFlow
                )
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { media ->
                updateState { copy(media = media) }
            },
            onError = { error ->
                updateState { copy(errorMessage = error.toString()) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            },
            checkSuccess = { true }
        )
    }

    override fun genreClicked(genreId: Int) {
    }
}

