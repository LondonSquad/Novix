package com.london.data.repository.list

import androidx.work.WorkManager
import com.london.data.local.model.customLists.MovieListMembershipLocal
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.mapper.list.toEntity
import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.orZero
import com.london.data.worker.MovieListSyncWorker
import com.london.domain.AppPreferencesService
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CustomMovieListRepositoryImpl @Inject constructor(
    private val remoteDataSource: CustomMovieListsRemoteDataSource,
    private val localDataSource: CustomMovieListLocalDataSource,
    private val authPreferences: AuthPreferences,
    private val preferencesService: AppPreferencesService,
    private val workManager: WorkManager
) : CustomMovieListRepository {

    private val syncMutex = Mutex()

    override suspend fun isMovieListed(movieId: Int, forceRefresh: Boolean): Boolean {
        refreshMovieListCacheIfNecessary(forceRefresh)
        return localDataSource.isMovieListed(movieId)
    }

    override fun isMovieListedFlow(movieId: Int): Flow<Boolean> {
        return localDataSource.isMovieListedFlow(movieId)
    }

    override suspend fun getMovieListIds(movieId: Int, forceRefresh: Boolean): List<Int> {
        refreshMovieListCacheIfNecessary(forceRefresh)
        return localDataSource.getMovieListIds(movieId)
    }

    override fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>> {
        return localDataSource.getMovieListIdsFlow(movieId)
    }

    override suspend fun deleteMovieList(id: Int): Boolean {
        val success = remoteDataSource.delete(
            listId = id, sessionId = authPreferences.getSessionId()
        ).isSuccess

        if (success) {
            localDataSource.removeMovieListCache(id)
        }

        return success
    }

    override suspend fun createMovieList(name: String): Boolean {
        val success = remoteDataSource.create(
            name = name,
            sessionId = authPreferences.getSessionId(),
            languageCode = preferencesService.appLanguage.value.code
        ).isSuccess

        if (success) {
            MovieListSyncWorker.syncNow(workManager)
        }

        return success
    }

    override suspend fun getMovieListName(listId: Int): String =
        remoteDataSource.getDetails(
            listId = listId,
            page = 1
        ).getOrThrow().name.orEmpty()

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

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean {
        val result = remoteDataSource.addMovieToList(
            listId = listId,
            movieId = movieId,
            sessionId = authPreferences.getSessionId()
        )

        return if (result.isSuccess) {
            localDataSource.addMovieToListCache(movieId, listId)
            true
        } else {
            false
        }
    }

    override suspend fun getMovieListDetails(
        listId: Int,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
        val response = remoteDataSource.getDetails(
            listId = listId,
            page = pageNumber
        ).getOrThrow()
        return PagedFetchResponse(
            currentPage = pageNumber,
            items = response.items.orEmpty().map { it.toEntity() },
            totalPages = MAX_PAGES,
            totalItems = response.itemCount.orZero()
        )
    }

    override suspend fun removeMovieFromList(
        listId: Int,
        movieId: Int
    ): Boolean {
        val result = remoteDataSource.removeMovieFromList(
            listId = listId,
            movieId = movieId,
            sessionId = authPreferences.getSessionId()
        )

        return if (result.isSuccess) {
            localDataSource.removeMovieFromListCache(movieId, listId)
            true
        } else {
            false
        }
    }

    private suspend fun refreshMovieListCacheIfNecessary(forceRefresh: Boolean) {
        if (forceRefresh || localDataSource.shouldRefreshCache()) refreshMovieListCache()
    }

    override suspend fun refreshMovieListCache() {
        syncMutex.withLock {
            try {
                val memberships = mutableListOf<MovieListMembershipLocal>()

                var page = 1
                do {
                    val listsResponse = remoteDataSource.getAllMovieLists(
                        page = page,
                        sessionId = authPreferences.getSessionId()
                    ).getOrThrow()

                    for (list in listsResponse.items) {
                        if (list.id == null) continue

                        var moviePage = 1
                        do {
                            val moviesResponse = remoteDataSource.getDetails(
                                listId = list.id,
                                page = moviePage
                            ).getOrThrow()


                            moviesResponse.items?.forEach { movie ->
                                if (movie.id == null) return@forEach

                                memberships.add(
                                    MovieListMembershipLocal(
                                        movieId = movie.id,
                                        listId = list.id
                                    )
                                )
                            }

                            moviePage++
                        } while (moviePage <= MAX_PAGES && moviesResponse.items?.isNotEmpty() == true)
                    }

                    page++
                } while (page <= listsResponse.totalPages)

                localDataSource.cacheMovieListMemberships(memberships)
                localDataSource.markCacheRefreshed(true)

            } catch (e: Exception) {
                localDataSource.markCacheRefreshed(false)
                throw e
            }
        }
    }

    private companion object {
        const val MAX_PAGES = 10
    }
}
