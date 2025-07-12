package com.london.data.repository

import android.util.Log
import com.london.data.datasource.local.GetException
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.search.RemoteDataSource
import com.london.data.datasource.util.CrashReporter
import com.london.data.mapper.toActorEntity
import com.london.data.mapper.toLocal
import com.london.data.mapper.toMovieEntity
import com.london.data.mapper.toTvShowEntity
import com.london.domain.ActorSearchFailedException
import com.london.domain.MovieSearchFailedException
import com.london.domain.TvShowSearchFailedException
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val searchTvShowService: LocalDataSource<SearchTvShowLocal>,
    private val searchActorService: LocalDataSource<SearchActorsLocal>,
    private val searchMovieService: LocalDataSource<SearchMoviesLocal>,
    private val remoteDataSource: RemoteDataSource,
    private val crashReporter: CrashReporter
) : SearchRepository {
    override suspend fun searchForMovies(
        name: String, language: String
    ): List<Movie> {

        var result: List<Movie> = emptyList()
        var remoteResponseToCache: SearchMoviesLocal? = null

        try {
            val local = searchMovieService.getByQuery(query = name + language)
            result = local
                ?.results
                ?.map { it.toMovieEntity() }
                ?: remoteDataSource.searchForMovies(
                    query = name,
                    language = language,
                    includeAdult = false,
                    page = 1,
                )
                    .toLocal(query = name + language)
                    .also { remoteResponseToCache = it }
                    .results
                    .map { it.toMovieEntity() }
            searchMovieService.insert(remoteResponseToCache ?: return result)
        } catch (_: GetException) {
            throw MovieSearchFailedException()
        } catch (e: Exception) {
            addExceptionToCrashlytics(e)
        }
        return result
    }

    override suspend fun searchForTvShows(
        name: String, language: String
    ): List<TvShow> {

        var localResults: List<TvShow> = emptyList()
        var remoteResponseToCache: SearchTvShowLocal? = null
        try {
            val local = searchTvShowService.getByQuery(query = name + language)
            localResults = local
                ?.results
                ?.map { it.toTvShowEntity() }
                ?: remoteDataSource.searchForTvShows(
                    query = name,
                    language = language,
                    includeAdult = false,
                    page = 1,
                )
                    .toLocal(query = name + language)
                    .also { remoteResponseToCache = it }
                    .results
                    .map { it.toTvShowEntity() }

            searchTvShowService.insert(remoteResponseToCache ?: return localResults)
        } catch (_: GetException) {
            throw TvShowSearchFailedException()
        } catch (e: Exception) {
            addExceptionToCrashlytics(e)
        }
        return localResults
    }

    override suspend fun searchForActors(
        name: String, language: String
    ): List<Actor> {

        var result: List<Actor> = emptyList()
        var remoteResponseToCache: SearchActorsLocal? = null
        try {
            val local = searchActorService.getByQuery(query = name + language)
            result = local
                ?.results
                ?.map { it.toActorEntity() }
                ?: remoteDataSource.searchForActors(
                    query = name,
                    language = language,
                    includeAdult = false,
                    page = 1,
                )
                    .toLocal(query = name + language)
                    .also { remoteResponseToCache = it }
                    .results.map { it.toActorEntity() }
            searchActorService.insert(remoteResponseToCache ?: return result)
        } catch (_: GetException) {
            throw ActorSearchFailedException()
        } catch (e: Exception) {
            addExceptionToCrashlytics(e)
        }
        return result
    }

    private fun addExceptionToCrashlytics(e: Exception) {
        crashReporter.logException(e)
    }
}
