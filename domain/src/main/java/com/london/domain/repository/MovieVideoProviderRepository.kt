package com.london.domain.repository

import com.london.domain.entity.videoprovider.MovieVideo

interface MovieVideoProviderRepository {
    suspend fun getMovieVideos(movieId: Int): List<MovieVideo>
}