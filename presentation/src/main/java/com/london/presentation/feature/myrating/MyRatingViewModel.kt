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

    fun refreshData() {
        initializeItems()
    }

    private fun initializeItems() {
        tryToExecute(
            block = {
                val movies = getRatedMovieUseCase.invoke()
                val tvShows = getRatedTvShowUseCase.invoke()
                val allRated = getAllRatedUseCase.invoke()
                Triple(movies, tvShows, allRated)
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { (movies, tvShows, allRated) ->
                updateState {
                    copy(
                        movies = movies,
                        tvShows = tvShows,
                        allRated = allRated,
                        isLoading = false
                    )
                }
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
        // TODO: Implement delete functionality
    }

    override fun onRatingCategorySelected(category: RatingCategory) {
        updateState { copy(selectedRatingCategory = category) }
    }

    override fun onItemClick(id: Int) {
        // TODO: Implement item click functionality
        emitEffect(MyRatingEffect.NavigateToMovie(id))
    }
}