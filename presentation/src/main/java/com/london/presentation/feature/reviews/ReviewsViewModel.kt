package com.london.presentation.feature.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.review.Review
import com.london.domain.entity.shared.MediaType
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.utils.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieUseCase: GetMovieUseCase,
    private val getTvShowUseCase: GetTvShowUseCase
) : BaseViewModel<ReviewsUiState, ReviewEffect>(ReviewsUiState()), ReviewContract {

    private val args = savedStateHandle.getArgs<Screen.Reviews>()
    private val mediaType: MediaType = args?.mediaType ?: MediaType.Movie
    private val mediaId: Int = args?.mediaId.orZero()

    init {
        loadReviews()
    }

    override fun onRetry() {
        setErrorState(null)
        loadReviews()
    }

    override fun onBackClicked() = emitEffect(ReviewEffect.NavigateBack)
    

    private fun loadReviews() {
        tryToExecute(
            block = { createPagingSourceFlow { _, pageNumber -> fetchReviewsByMediaType(pageNumber) } },
            onStart = { setLoadingState(true) },
            onSuccess = ::handleLoadReviewsSuccess,
            onError = ::handleLoadReviewsError,
            onCompleted = { setLoadingState(false) }
        )
    }

    private fun handleLoadReviewsSuccess(pagingFlow: Flow<PagingData<Review>>) {
        setLoadingState(false)
        setErrorState(null)
        updateState { copy(reviews = pagingFlow) }
    }

    private fun handleLoadReviewsError(errorState: ErrorState) {
        setErrorState(errorState)
        setLoadingState(false)
    }

    private suspend fun fetchReviewsByMediaType(pageNumber: Int) = when (mediaType) {
        MediaType.Movie -> getMovieUseCase.getMovieReviews(mediaId, pageNumber)
        MediaType.TvShow -> getTvShowUseCase.getTvShowReviews(mediaId, pageNumber)
    }

    private fun setLoadingState(loading: Boolean) = updateState { copy(isLoading = loading) }

    private fun setErrorState(errorState: ErrorState?) = updateState { copy(error = errorState) }
}
