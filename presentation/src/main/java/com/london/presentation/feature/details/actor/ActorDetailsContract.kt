package com.london.presentation.feature.details.actor

interface ActorDetailsContract {
    fun onRetry()
    fun onNavigateBack()
    fun onGalleryClick(actorId: Int)
    fun onMoviePicksClick(actorId: Int)
    fun onMovieScreenClick(movieId: Int)
    fun onTvShowPicksClick(actorId: Int)
    fun onTvShowScreenClick(tvShowId: Int)
}