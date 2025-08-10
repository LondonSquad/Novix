package com.london.presentation.feature.accountinfo.rating

import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.presentation.feature.reviews.MediaType
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val manageRatingUseCase: ManageRatingUseCase
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingContract {

    init {
        initializeItems()
    }

    fun initializeItems() {
        tryToExecute(
            block = {
                manageRatingUseCase.getRatedMovies()
                manageRatingUseCase.getRatedTvShows()
                RatingData(
                    ratedMovies = manageRatingUseCase.getRatedMovies(),
                    ratedTvShows = manageRatingUseCase.getRatedTvShows(),
                )
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { ratedMedia ->
                updateState {
                    copy(
                        ratedMovies = ratedMedia.ratedMovies,
                        ratedTvShows = ratedMedia.ratedTvShows,
                        allRatedMedia = ratedMedia.ratedMovies + ratedMedia.ratedTvShows,
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        errorState = errorState,
                    )
                }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            },
        )
    }

    override fun onBackClicked() = emitEffect(MyRatingEffect.NavigateBack)

    override fun onMovieClick(id: Int) =
        emitEffect(MyRatingEffect.NavigateToMovie(id))

    override fun onTvShowClick(id: Int) =
        emitEffect(MyRatingEffect.NavigateToTvShow(id))

    override fun onDelete(id: Int, mediaType: MediaType) {
        tryToExecute(
            block = {
                if (mediaType == MediaType.Movie)
                    manageRatingUseCase.deleteMovieRating(id)
                else if (mediaType == MediaType.TvShow)
                    manageRatingUseCase.deleteTvShowRating(id)
            },
            onStart = { updateState { copy(isSnackBarVisible = false) } },
            onSuccess = {
                val updatedMovies = manageRatingUseCase.getRatedMovies(id)
                val updatedTvShows = manageRatingUseCase.getRatedTvShows(id)
                updateState {
                    copy(
                        ratedMovies = updatedMovies,
                        ratedTvShows = updatedTvShows,
                        allRatedMedia = updatedMovies + updatedTvShows
                    )
                }
            },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isSnackBarVisible = true) } },
        )
    }

    override fun onRatingCategorySelected(category: RatingCategory) {
        updateState { copy(selectedRatingCategory = category) }
    }

    override fun onItemClick(id: Int) {
        emitEffect(MyRatingEffect.NavigateToMovie(id))
    }
}