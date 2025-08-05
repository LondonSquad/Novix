package com.london.data.repository.list

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.list.toEntity
import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.domain.AppPreferencesService
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class CustomMovieListRepositoryImpl @Inject constructor(
    private val remoteDataSource: CustomMovieListsRemoteDataSource,
    private val authPreferences: AuthPreferences,
    private val preferencesService: AppPreferencesService
) : CustomMovieListRepository {

    override suspend fun deleteMovieList(id: UInt): Boolean =
        remoteDataSource.delete(
            listId = id.toInt(),
            sessionId = authPreferences.getSessionId()
        ).isSuccess

    override suspend fun createMovieList(name: String): Boolean =
        remoteDataSource.create(
            name = name,
            sessionId = authPreferences.getSessionId(),
            languageCode = preferencesService.appLanguage.value.code
        ).isSuccess

    override suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> {
        val response = remoteDataSource.getAllMovieLists(
            page = pageNumber,
            sessionId = authPreferences.getSessionId()
        ).getOrThrow()

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun addMovieToList(listId: UInt, movieId: UInt): Boolean =
        remoteDataSource.addMovieToList(
            listId = listId.toInt(),
            movieId = movieId.toInt(),
            sessionId = authPreferences.getSessionId()
        ).isSuccess

    override suspend fun getMovieListDetails(
        listId: UInt,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
        val response = remoteDataSource.getDetails(
            listId = listId.toInt(),
            page = pageNumber
        ).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }
}
