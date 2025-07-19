package com.london.presentation.screen.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.usecase.reviews.GetMovieReviewsUseCase
import com.london.domain.usecase.reviews.GetTvShowReviewsUseCase
import com.london.presentation.navigation.arguments.ReviewsScreenArgs
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReviewsViewModel(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getTvShowReviewsUseCase: GetTvShowReviewsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val args = ReviewsScreenArgs(savedStateHandle)
        initializeReviews(args.mediaType, args.mediaId)
    }

    fun initializeReviews(mediaType: Int, mediaId: Int) {
        launchCatching {
            _uiState.update {
                it.copy(
                    reviews = createPagingSourceFlow<ReviewEntity>("") { _, pageNumber ->
                        when (mediaType) {
                            MediaType.Movie.mediaNum -> getMovieReviewsUseCase.invoke(mediaId, pageNumber)
                            else -> getTvShowReviewsUseCase.invoke(mediaId, pageNumber)
                        }
                    }
                )
            }
        }
    }
}