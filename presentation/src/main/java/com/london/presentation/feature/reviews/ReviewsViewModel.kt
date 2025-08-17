package com.london.presentation.feature.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getTvShowUseCase: GetTvShowUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ReviewsUiState, ReviewEffect>(ReviewsUiState()), ReviewContract {

    private val args = savedStateHandle.getArgs<Screen.Reviews>()
    private val mediaType: MediaType = args?.mediaType ?: MediaType.Movie
    private val mediaId: Int = args?.mediaId ?: 0

    init {
        loadReviews()
    }

    override fun onRetry() {
        updateState { copy(error = null) }
        loadReviews()
    }

    override fun onBackClicked() {
        emitEffect(ReviewEffect.NavigateBack)
    }

    private fun loadReviews() {
        tryToExecute(
            block = {
                createPagingSourceFlow { _, pageNumber ->
                    fetchReviewsByMediaType(pageNumber)
                }
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = ::handleLoadReviewsSuccess,
            onError = ::handleLoadReviewsError,
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    private fun handleLoadReviewsSuccess(pagingFlow: Flow<PagingData<ReviewEntity>>) {
        updateState {
            copy(
                reviews = pagingFlow,
                isLoading = false,
                error = null
            )
        }
    }

    private fun handleLoadReviewsError(errorState: ErrorState) {
        updateState {
            copy(
                error = errorState,
                isLoading = false
            )
        }
    }

    private suspend fun fetchReviewsByMediaType(pageNumber: Int) = when (mediaType) {
        MediaType.Movie -> getMovieUseCase.getMovieReviews(mediaId, pageNumber)
        MediaType.TvShow -> getTvShowUseCase.getTvShowReviews(mediaId, pageNumber)
    }
}