package com.london.presentation.feature.myrating

import com.london.domain.usecase.GetRatingUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val getRatingUseCase: GetRatingUseCase
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingContract {

    init {
        initializeItems()
    }

    fun initializeItems() {
        tryToExecute(
            block = {
                val allRatedMedia = getRatingUseCase.getAllRated()
                val ratedMovies = getRatingUseCase.getRatedMovies()
                val ratedTvShows = getRatingUseCase.getRatedTvShows()
                RatingData(allRatedMedia, ratedMovies, ratedTvShows)
            },
            onStart = {
                updateState { copy(isLoading = true, errorState = null) }
            },
            onSuccess = { ratingData ->
                updateState {
                    copy(
                        allRatedMedia = ratingData.allRatedMedia,
                        ratedMovies = ratingData.ratedMovies,
                        ratedTvShows = ratingData.ratedTvShows,
                        isLoading = false,
                        errorState = null
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        errorState = errorState,
                        isLoading = false
                    )
                }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            },
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
        emitEffect(MyRatingEffect.NavigateToMovie(id))
    }
}