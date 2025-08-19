package com.london.data.repository.list

import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.model.customLists.MovieListMembershipLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.mapper.list.toEntity
import com.london.data.mapper.list.toLocal
import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.data.utils.orZero
import com.london.domain.AppPreferencesService
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieList
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CustomMovieListRepositoryImpl @Inject constructor(
    private val remoteDataSource: CustomMovieListsRemoteDataSource,
    private val localDataSource: CustomMovieListLocalDataSource,
    private val authenticationPreferences: AuthenticationPreferences,
    private val preferencesService: AppPreferencesService,
    private val crashReporter: CrashReporter
) : CustomMovieListRepository {

    private val syncMutex = Mutex()

    override suspend fun isMovieListed(movieId: Int, forceRefresh: Boolean): Boolean {
        return fetchAndSync(
            cacheBlock = if (!forceRefresh) {
                { localDataSource.isMovieListed(movieId) }
            } else null,
            networkBlock = {
                refreshMovieListCacheIfNecessary(forceRefresh = true)
                localDataSource.isMovieListed(movieId)
            },
            crashReporter = crashReporter
        )
    }

    override fun isMovieListedFlow(movieId: Int): Flow<Boolean> =
        localDataSource.isMovieListedFlow(movieId)

    override suspend fun getMovieListIds(movieId: Int, forceRefresh: Boolean): List<Int> {
        return fetchAndSync(
            cacheBlock = if (!forceRefresh) {
                { localDataSource.getMovieListIds(movieId) }
            } else null,
            networkBlock = {
                refreshMovieListCacheIfNecessary(forceRefresh = true)
                localDataSource.getMovieListIds(movieId)
            },
            crashReporter = crashReporter
        )
    }

    override fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>> =
        localDataSource.getMovieListIdsFlow(movieId)

    override suspend fun deleteMovieList(id: Int): Boolean {
        return try {
            val success = remoteDataSource.delete(
                listId = id,
                sessionId = authenticationPreferences.getSessionId()
            ).isSuccess

            if (success) {
                localDataSource.removeMovieListCache(id)
            }

            success
        } catch (e: Exception) {
            crashReporter.logException(e)
            false
        }
    }

    override suspend fun getAllListedMovieIds(): List<Int> {
        refreshMovieListCacheIfNecessary(forceRefresh = false)
        return localDataSource.getAllListedMovieIds()
    }

    override fun getAllListedMovieIdsFlow(): Flow<List<Int>> =
        localDataSource.getAllListedMovieIdsFlow()

    override suspend fun createMovieList(name: String): Boolean {
        return try {
            val result = remoteDataSource.create(
                name = name,
                sessionId = authenticationPreferences.getSessionId(),
                languageCode = preferencesService.appLanguage.value.code
            )

            if (result.isSuccess) {
                result.getOrNull()?.let { response ->
                    response.id?.let {
                        localDataSource.addMovieListCache(
                            MovieListLocal(
                                id = response.id,
                                name = name,
                                description = "",
                                itemCount = 0
                            )
                        )
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            crashReporter.logException(e)
            false
        }
    }

    override suspend fun getMovieListName(listId: Int): String {
        return fetchAndSync(
            cacheBlock = { localDataSource.getMovieList(listId)?.name },
            networkBlock = {
                remoteDataSource.getDetails(listId = listId, page = 1).getOrThrow().name.orEmpty()
            },
            syncBlock = { name ->
                localDataSource.getMovieList(listId)?.let { existing ->
                    localDataSource.addMovieListCache(existing.copy(name = name))
                }
            },
            crashReporter = crashReporter
        )
    }

    override suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> {
        return if (localDataSource.shouldRefreshCache()) {
            val response = remoteDataSource.getAllMovieLists(
                page = pageNumber,
                sessionId = authenticationPreferences.getSessionId()
            ).getOrThrow()

            val localLists = response.items.map { it.toLocal() }
            localDataSource.cacheMovieListsMetadata(localLists)

            PagedFetchResponse(
                currentPage = response.currentPage,
                items = response.items.map { it.toEntity() },
                totalPages = response.totalPages,
                totalItems = response.totalItems
            )
        } else {
            val allLists = localDataSource.getAllUserLists()
            val itemsPerPage = 20
            val startIndex = (pageNumber - 1) * itemsPerPage
            val endIndex = minOf(startIndex + itemsPerPage, allLists.size)

            val items = if (startIndex < allLists.size) {
                allLists.subList(startIndex, endIndex).map { it.toEntity() }
            } else {
                emptyList()
            }

            PagedFetchResponse(
                currentPage = pageNumber,
                items = items,
                totalPages = (allLists.size + itemsPerPage - 1) / itemsPerPage,
                totalItems = allLists.size
            )
        }
    }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean {
        return try {
            val result = remoteDataSource.addMovieToList(
                listId = listId,
                movieId = movieId,
                sessionId = authenticationPreferences.getSessionId()
            )

            if (result.isSuccess) {
                localDataSource.addMovieToListCache(movieId, listId)

                localDataSource.getMovieList(listId)?.let { list ->
                    localDataSource.addMovieListCache(
                        list.copy(itemCount = list.itemCount + 1)
                    )
                }

                true
            } else {
                false
            }
        } catch (e: Exception) {
            crashReporter.logException(e)
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

    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean {
        return try {
            val result = remoteDataSource.removeMovieFromList(
                listId = listId,
                movieId = movieId,
                sessionId = authenticationPreferences.getSessionId()
            )

            if (result.isSuccess) {
                localDataSource.removeMovieFromListCache(movieId, listId)

                localDataSource.getMovieList(listId)?.let { list ->
                    localDataSource.addMovieListCache(
                        list.copy(itemCount = maxOf(0, list.itemCount - 1))
                    )
                }

                true
            } else {
                false
            }
        } catch (e: Exception) {
            crashReporter.logException(e)
            false
        }
    }

    override suspend fun refreshMovieListCache() {
        syncMutex.withLock {
            try {
                val memberships = mutableListOf<MovieListMembershipLocal>()
                val lists = mutableListOf<MovieListLocal>()

                var page = 1
                do {
                    val listsResponse = remoteDataSource.getAllMovieLists(
                        page = page,
                        sessionId = authenticationPreferences.getSessionId()
                    ).getOrThrow()

                    lists.addAll(listsResponse.items.map { it.toLocal() })

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

                localDataSource.cacheMovieListsMetadata(lists)
                localDataSource.cacheMovieListMemberships(memberships)
                localDataSource.markCacheRefreshed(true)

            } catch (e: Exception) {
                localDataSource.markCacheRefreshed(false)
                crashReporter.logException(e)
                throw e
            }
        }
    }

    private suspend fun refreshMovieListCacheIfNecessary(forceRefresh: Boolean) {
        if (forceRefresh || localDataSource.shouldRefreshCache()) refreshMovieListCache()
    }

    private companion object {
        const val MAX_PAGES = 10
    }
}

