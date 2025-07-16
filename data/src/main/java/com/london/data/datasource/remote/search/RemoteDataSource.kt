package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote

interface RemoteDataSource {
    suspend fun searchForMovies(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchMovieRemote>
    suspend fun searchForTvShows(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchTvShowRemote>
    suspend fun searchForActors(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchActorRemote>

}
