package com.london.presentation.feature.search.details.movie

interface MovieDetailsContract {
    fun onBackClick()
    fun onSavedClick()
    fun onExpandClick()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onReviewsClick(movieId: Int, mediaNumber: Int)
    fun onGenreClick(genreId: Int)
    fun onRetry()
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
    fun onLoginClick()
}