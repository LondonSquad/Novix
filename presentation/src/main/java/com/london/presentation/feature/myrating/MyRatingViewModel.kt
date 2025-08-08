package com.london.presentation.feature.myrating

import com.london.domain.usecase.rating.GetAllRatedUseCase
import com.london.domain.usecase.rating.GetRatedMovieUseCase
import com.london.domain.usecase.rating.GetRatedTvShowUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val getAllRatedUseCase: GetAllRatedUseCase,
    private val getRatedMovieUseCase: GetRatedMovieUseCase,
    private val getRatedTvShowUseCase: GetRatedTvShowUseCase
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingContract {

    init {
        initializeItems()
    }

    private fun initializeItems() {
        tryToExecute(
            block = {
                val movies = getRatedMovieUseCase.invoke()
                val tvShows = getRatedTvShowUseCase.invoke()
                val allRated = getAllRatedUseCase.invoke()
                movies to tvShows to allRated
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { movies, tvShows, allRated ->
                updateState { copy(movieFlow = movies, tvShowFlow = tvShows, allRated = allRated) }
            },
            onError = {

            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onBackClicked() = emitEffect(MyRatingEffect.NavigateBack)

    override fun onMovieClick(id: Int) = emitEffect(MyRatingEffect.NavigateToMovie(id))

    override fun onTvShowClick(id: Int) = emitEffect(MyRatingEffect.NavigateToTvShow(id))

    override fun onDelete(id: Int) {
        TODO("Not yet implemented")
    }

}