package com.london.data.datasource.remote.details.videoprovider.movie

import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoRemote

interface MovieVideoProviderRemote {
    suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote>
}