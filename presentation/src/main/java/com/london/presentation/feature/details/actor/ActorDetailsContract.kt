package com.london.presentation.feature.details.actor

interface ActorDetailsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onActorGalleryClick(actorId: Int)
    fun onTopMoviePicksClick(actorId: Int)
    fun onMovieScreenClick(movieId: Int)
    fun onTopTvShowPicksClick(actorId: Int)
    fun onTvShowScreenClick(tvShowId: Int)
    fun onBookmarkSheetDismiss()
    fun onManageBookmarkClicked(movieId: Int)
}
