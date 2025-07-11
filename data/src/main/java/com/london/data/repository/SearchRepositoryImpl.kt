package com.london.data.repository

import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.RemoteDataSource
import com.london.data.mapper.toActorEntity
import com.london.data.mapper.toMovieEntity
import com.london.data.mapper.toTvShowEntity
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val searchTvShowService: LocalDataSource<SearchTvShowLocal>,
    private val searchActorService: LocalDataSource<SearchActorsLocal>,
    private val searchMovieService: LocalDataSource<SearchMoviesLocal>,
    private val remoteDataSource: RemoteDataSource
) : SearchRepository {
    override suspend fun searchForMovies(
        name: String, language: String
    ): List<Movie> {
        val local = searchMovieService.getByQuery(query = name + language)
        return local?.results?.map { it.toMovieEntity() }
            ?: remoteDataSource.searchForMovies().results.map { it.toMovieEntity() }
    }

    override suspend fun searchForTvShows(
        name: String, language: String
    ): List<TvShow> {
        val local = searchTvShowService.getByQuery(query = name + language)
        return local?.results?.map { it.toTvShowEntity() }
            ?: remoteDataSource.searchForTvShows().results.map { it.toTvShowEntity() }
    }

    override suspend fun searchForActors(
        name: String, language: String
    ): List<Actor> {
        val local = searchActorService.getByQuery(query = name + language)
        return local?.results?.map { it.toActorEntity() }
            ?: remoteDataSource.searchForActors().results.map { it.toActorEntity() }
    }
}