package com.london.presentation.feature.details.actor

interface ActorDetailsContract {
    fun onNavigateBack()
    fun onRetry()
    fun onGalleryClick(actorId: Int)
    fun onTvShowPicksClick(actorId: Int)
    fun onTvShowScreenClick(tvShowId: Int)
    fun onMoviePicksClick(actorId: Int)
    fun onMovieScreenClick(movieId: Int)
}