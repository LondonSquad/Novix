package com.london.data.datasource.remote

import com.london.data.datasource.remote.search.SearchActorsResponse
import com.london.data.datasource.remote.search.SearchMoviesResponse
import com.london.data.datasource.remote.search.SearchTvShowsResponse

interface RemoteDataSource {
    suspend fun searchForMovies(): SearchMoviesResponse
    suspend fun searchForTvShows(): SearchTvShowsResponse
    suspend fun searchForActors(): SearchActorsResponse

}