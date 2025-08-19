package com.london.presentation.feature.details.movie

import com.london.domain.entity.shared.MediaType
import com.london.presentation.shared.genre.MovieGenreUi

interface MovieDetailsContract {
    fun onRetryClick()
    fun onBackClick()
    fun onLoginClick()
    fun onSavedClick()
    fun onExpandClick()
    fun onRateBottomSheetClick()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onSelectRatingClick(rating: Int)
    fun onGenreClick(genre: MovieGenreUi)
    fun onReviewsClick(movieId: Int, mediaType: MediaType)
}