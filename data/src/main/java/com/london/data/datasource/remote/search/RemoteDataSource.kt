package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.datasource.remote.`\`.ActorDetailsDto
import com.london.data.datasource.remote.actordetails.ActorMovieDetailsDto
import com.london.data.datasource.remote.actordetails.ActorTvShowDetailsDto

interface RemoteDataSource {
    suspend fun searchForMovies(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchMovieRemote>
    suspend fun searchForTvShows(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchTvShowRemote>
    suspend fun searchForActors(query: String, includeAdult: Boolean, language: String, page: Int)
    : ApiSearch<SearchActorRemote>
    suspend fun getActorDetails(actorId: Int): ActorDetailsDto
    suspend fun getActorMovies(actorId: Int): ActorMovieDetailsDto
    suspend fun getActorTvShows(actorId: Int): ActorTvShowDetailsDto
}
