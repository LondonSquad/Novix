package com.london.presentation.screen.details.actor

interface ActorDetailsContract {
    fun onNavigateBack()
    fun onGalleryClick(actorId: Int)
    fun onTvShowPicksClick(actorId: Int)
    fun onTvShowScreenClick(tvShowId: Int)
    fun onMoviePicksClick(actorId: Int)
    fun onMovieScreenClick(movieId: Int)
}