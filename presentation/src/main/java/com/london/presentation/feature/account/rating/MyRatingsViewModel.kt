package com.london.presentation.feature.account.rating

import com.london.domain.entity.RatedMedia
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

    override fun onRatingCategorySelected(category: RatingCategory) =
        updateState { copy(selectedRatingCategory = category) }

    override fun onItemClick(id: Int) {
        val ratedMedia = state.value.allRatedMedia.find { it.id == id }
            ?: state.value.ratedMovies.find { it.id == id }
            ?: state.value.ratedTvShows.find { it.id == id }
        
        ratedMedia?.let { rated ->
            when (rated.mediaType) {
                MediaType.Movie -> emitEffect(MyRatingEffect.NavigationMovieDetails(id))
                MediaType.TvShow -> emitEffect(MyRatingEffect.NavigationTvShowDetails(id))
            }
        }
    }

    override fun onRetryClick() = initializeRatedMedia()

    override fun onBackClick() = emitEffect(MyRatingEffect.NavigationBack)

    override fun onMovieClick(id: Int) = emitEffect(MyRatingEffect.NavigationMovieDetails(id))

    override fun onTvShowClick(id: Int) = emitEffect(MyRatingEffect.NavigationTvShowDetails(id))

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
}
