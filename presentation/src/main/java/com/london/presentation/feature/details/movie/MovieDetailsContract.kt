package com.london.presentation.feature.details.movie

import com.london.domain.entity.recent.MediaType

interface MovieDetailsContract {
    fun onRetry()
    fun onBackClick()
    fun onLoginClick()
    fun onSavedClick()
    fun onExpandClick()
    fun onRateBottomSheetClick()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onGenreClick(genreId: Int)
    fun onSelectRatingClick(rating: Int)
    fun onReviewsClick(movieId: Int, mediaType: MediaType)
}