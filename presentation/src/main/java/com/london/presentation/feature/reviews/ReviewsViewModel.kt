package com.london.presentation.feature.reviews

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.reviews.GetMovieReviewsUseCase
import com.london.domain.usecase.reviews.GetTvShowReviewsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReviewsViewModel(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getTvShowReviewsUseCase: GetTvShowReviewsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ReviewsUiState, ReviewEffect>(ReviewsUiState()), ReviewContract {

    private val args = savedStateHandle.getArgs<Screen.Reviews>()
    private val mediaType = args?.mediaType ?: 0
    private val mediaId = args?.mediaId ?: 0

    init {
        initializeReviews(mediaType, mediaId)
    }

    override fun onBackClicked() {
        emitEffect(ReviewEffect.NavigateBack)
    }

    private fun initializeReviews(mediaType: Int, mediaId: Int) {
        tryToExecute(
            block = {
                createPagingSourceFlow("") { _, pageNumber ->
                    when (mediaType) {
                        MediaType.Movie.mediaNum -> getMovieReviewsUseCase.invoke(
                            mediaId,
                            pageNumber
                        )

                        else -> getTvShowReviewsUseCase.invoke(mediaId, pageNumber)
                    }
                }
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { pagingFlow ->
                updateState {
                    copy(reviews = pagingFlow)
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }
}