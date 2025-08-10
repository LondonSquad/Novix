package com.london.presentation.feature.accountinfo.rating

import com.london.domain.entity.recent.MediaType
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val manageRatingUseCase: ManageRatingUseCase
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingsContract {

    init {
        initializeRatedMedia()
    }

    fun initializeRatedMedia() {
        tryToExecute(
            block = { manageRatingUseCase.getRatedMediaSorted() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { ratedMedia ->
                updateState {
                    copy(
                        ratedMovies = ratedMedia.filter { it.mediaType == MediaType.Movie },
                        ratedTvShows = ratedMedia.filter { it.mediaType == MediaType.TvShow },
                        allRatedMedia = ratedMedia,
                    )
                }
            },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onDeleteMovie(id: Int) {
        tryToExecute(
            block = { manageRatingUseCase.deleteMovieRating(id) },
            onStart = { updateState { copy(isSnackBarVisible = false) } },
            onSuccess = {
                updateState {
                    copy(
                        ratedMovies = ratedMovies.filter { it.id != id },
                        allRatedMedia = allRatedMedia.filter { it.id != id }
                    )
                }
            },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isSnackBarVisible = true) } },
        )
    }

    override fun onDeleteShow(id: Int) {
        tryToExecute(
            block = { manageRatingUseCase.deleteTvShowRating(id) },
            onStart = { updateState { copy(isSnackBarVisible = false) } },
            onSuccess = {
                updateState {
                    copy(
                        ratedTvShows = ratedTvShows.filter { it.id != id },
                        allRatedMedia = allRatedMedia.filter { it.id != id }
                    )
                }
            },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isSnackBarVisible = true) } },
        )
    }

    override fun onRatingCategorySelected(category: RatingCategory) =
        updateState { copy(selectedRatingCategory = category) }

    override fun onItemClick(id: Int) =
        emitEffect(MyRatingEffect.NavigateToMovie(id))

    override fun onBackClicked() = emitEffect(MyRatingEffect.NavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(MyRatingEffect.NavigateToMovie(id))

    override fun onTvShowClick(id: Int) =
        emitEffect(MyRatingEffect.NavigateToTvShow(id))
}
