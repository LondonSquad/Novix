package com.london.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.london.data.datasource.local.GetException
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.RemoteDataSource
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
    private val remoteDataSource: RemoteDataSource
) : SearchRepository {
    override suspend fun searchForMovies(
        name: String, language: String
    ): List<Movie> {

        var result: List<Movie> = emptyList()
        val local = searchMovieService.getByQuery(query = name + language)
        var remoteResponseToCache: SearchMoviesLocal? = null
        try {
            result = local
                ?.results
                ?.map { it.toMovieEntity() }
                ?: remoteDataSource.searchForMovies()
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

        var result: List<TvShow> = emptyList()
        val local = searchTvShowService.getByQuery(query = name + language)
        var remoteResponseToCache: SearchTvShowLocal? = null
        try {
            result = local
                ?.results
                ?.map { it.toTvShowEntity() }
                ?: remoteDataSource.searchForTvShows()
                    .toLocal(query = name + language)
                    .also { remoteResponseToCache = it }
                    .results
                    .map { it.toTvShowEntity() }
            searchTvShowService.insert(remoteResponseToCache ?: return result)
        } catch (_: GetException) {
            throw TvShowSearchFailedException()
        } catch (e: Exception) {
            addExceptionToCrashlytics(e)
        }
        return result
    }

    override suspend fun searchForActors(
        name: String, language: String
    ): List<Actor> {

        var result: List<Actor> = emptyList()
        val local = searchActorService.getByQuery(query = name + language)
        var remoteResponseToCache: SearchActorsLocal? = null
        try {
            result = local
                ?.results
                ?.map { it.toActorEntity() }
                ?: remoteDataSource.searchForActors()
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
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("operation", "un_known_error")
            setCustomKey("error_type", "{${e.javaClass.name}}")
            setCustomKey("timestamp", System.currentTimeMillis())
            recordException(e)
        }
    }
}
