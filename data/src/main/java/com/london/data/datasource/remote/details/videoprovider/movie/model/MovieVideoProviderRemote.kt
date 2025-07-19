package com.london.data.datasource.remote.details.videoprovider.movie.model

interface MovieVideoProviderRemote {
    suspend fun getMovieVideos(movieId: Int): MovieVideoResponse
}