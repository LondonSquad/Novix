package com.london.presentation.features.reviews

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.reviews.GetMovieReviewsUseCase
import com.london.domain.usecase.reviews.GetTvShowReviewsUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.features.base.BaseViewModel
import com.london.presentation.features.base.createPagingSourceFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReviewsViewModel(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getTvShowReviewsUseCase: GetTvShowReviewsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ReviewsUiState, ReviewsUiEffect>(ReviewsUiState()), ReviewsContract {
    private val args: Screen.Reviews? = savedStateHandle.getArgs<Screen.Reviews>()

    init {
        initializeReviews()
    }

    override fun navigateBack() = emitEffect(ReviewsUiEffect.OnNavigateBack)
    override fun showReviewDetails(reviewId: Int) = emitEffect(
        ReviewsUiEffect.OnNavigateToReviewDetails(reviewId)
    )

    private fun initializeReviews() = tryToExecute(
        block = {
            createPagingSourceFlow { pageNumber ->
                if (args == null) return@createPagingSourceFlow PagedFetchResponse()

                when (args.mediaType) {
                    MediaType.Movie.mediaNum -> getMovieReviewsUseCase.invoke(
                        args.mediaId,
                        pageNumber
                    )

                    else -> getTvShowReviewsUseCase.invoke(args.mediaId, pageNumber)
                }
            }
        },
        onStart = { updateState { copy(isLoading = true) } },
        onSuccess = { reviews -> updateState { copy(reviews = reviews) } },
        onError = { errorState -> updateState { copy(error = errorState) } },
        onCompleted = { updateState { copy(isLoading = false) } },
    )
}
