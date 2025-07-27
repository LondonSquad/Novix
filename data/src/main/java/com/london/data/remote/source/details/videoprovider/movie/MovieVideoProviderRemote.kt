package com.london.data.remote.source.details.videoprovider.movie

import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemote


interface MovieVideoProviderRemote {
    suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote>
}