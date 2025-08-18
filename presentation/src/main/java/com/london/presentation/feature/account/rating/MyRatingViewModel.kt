package com.london.presentation.feature.account.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val manageRatingUseCase: ManageRatingUseCase
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingContract {

    init {
        initializeRatedMedia()
    }

    fun initializeRatedMedia() {
        tryToExecute(
            block = { manageRatingUseCase.getRatedMediaSorted() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { ratedMedia -> updateStateRatedMedia(ratedMedia) },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onDeleteMovieClick(id: Int) {
        tryToExecute(
            block = { manageRatingUseCase.deleteMovieRating(id) },
            onStart = { updateState { copy(isSnackBarVisible = false) } },
            onSuccess = { updateStateAfterMediaDeletion(id) },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isSnackBarVisible = true) } },
        )
    }

    override fun onDeleteTVShowClick(id: Int) {
        tryToExecute(
            block = { manageRatingUseCase.deleteTvShowRating(id) },
            onStart = { updateState { copy(isSnackBarVisible = false) } },
            onSuccess = { updateStateAfterMediaDeletion(id) },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isSnackBarVisible = true) } },
        )
    }

    override fun onItemClick(id: Int) {
        tryToExecute(
            block = { manageRatingUseCase.getRatedMediaById(id) },
            onSuccess = { ratedMedia -> handleRatedMediaNavigation(ratedMedia, id) },
            onError = { errorState -> updateState { copy(errorState = errorState) } }
        )
    }

    override fun onRatingCategorySelected(category: RatingCategory) =
        updateState { copy(selectedRatingCategory = category) }

    override fun onRetryClick() = initializeRatedMedia()

    override fun onBackClick() = emitEffect(MyRatingEffect.BackNavigation)

    override fun onMovieClick(id: Int) = emitEffect(MyRatingEffect.MovieDetailsNavigation(id))

    override fun onTvShowClick(id: Int) = emitEffect(MyRatingEffect.TvShowDetailsNavigation(id))

    private fun updateStateRatedMedia(ratedMedia: List<RatedMedia>) {
        updateState {
            copy(
                ratedMovies = ratedMedia.filter { it.mediaType == MediaType.Movie },
                ratedTvShows = ratedMedia.filter { it.mediaType == MediaType.TvShow },
                allRatedMedia = ratedMedia,
            )
        }
    }

    private fun updateStateAfterMediaDeletion(id: Int) {
        updateState {
            copy(
                ratedMovies = ratedMovies.filter { it.id != id },
                ratedTvShows = ratedTvShows.filter { it.id != id },
                allRatedMedia = allRatedMedia.filter { it.id != id }
            )
        }
    }

    private fun handleRatedMediaNavigation(ratedMedia: RatedMedia?, id: Int) {
        ratedMedia?.let { rated ->
            val effect = when (rated.mediaType) {
                MediaType.Movie -> MyRatingEffect.MovieDetailsNavigation(id)
                MediaType.TvShow -> MyRatingEffect.TvShowDetailsNavigation(id)
            }
            emitEffect(effect)
        }
    }
}
