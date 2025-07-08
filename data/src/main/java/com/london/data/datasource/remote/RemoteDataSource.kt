package com.london.data.datasource.remote

import com.london.data.dto.search.SearchActorsResponse
import com.london.data.dto.search.SearchMoviesResponse
import com.london.data.dto.search.SearchTvShowsResponse

interface RemoteDataSource {
    suspend fun searchForMovies(): SearchMoviesResponse
    suspend fun searchForTvShows(): SearchTvShowsResponse
    suspend fun searchForActors(): SearchActorsResponse

}