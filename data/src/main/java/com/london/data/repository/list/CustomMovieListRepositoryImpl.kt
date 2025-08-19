package com.london.data.repository.list

import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.model.customLists.MovieListMembershipLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.mapper.list.toEntity
import com.london.data.mapper.list.toLocal
import com.london.data.mapper.search.toEntity
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.CrashReporter
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
        val success = deleteListRemotely(id)
        if (success) {
            localDataSource.removeMovieListCache(id)
        }
        return success
    }

    private suspend fun deleteListRemotely(id: Int): Boolean {
        return runCatching {
            remoteDataSource.delete(
                listId = id,
                sessionId = authenticationPreferences.getSessionId()
            ).isSuccess
        }.onFailure {
            crashReporter.logException(it)
            throw it
        }.isSuccess
    }

    override suspend fun getAllListedMovieIds(): List<Int> {
        refreshMovieListCacheIfNecessary(forceRefresh = false)
        return localDataSource.getAllListedMovieIds()
    }

    override fun getAllListedMovieIdsFlow(): Flow<List<Int>> {
        return localDataSource.getAllListedMovieIdsFlow()
    }

    override suspend fun createMovieList(name: String): Boolean {
        val response = createListRemotely(name)
        cacheNewlyCreatedList(response, name)
        return true
    }

    private suspend fun createListRemotely(name: String): CreateCustomListResponse {
        return remoteDataSource.create(
            name = name,
            sessionId = authenticationPreferences.getSessionId(),
            languageCode = preferencesService.appLanguage.value.code
        ).getOrThrow()
    }

    private suspend fun cacheNewlyCreatedList(response: CreateCustomListResponse, name: String) {
        response.id?.let { listId ->
            localDataSource.addMovieListCache(
                MovieListLocal(
                    id = listId,
                    name = name,
                    description = "",
                    itemCount = 0
                )
            )
        }
    }

    override suspend fun getMovieListName(listId: Int): String {
        refreshMovieListCacheIfNecessary(forceRefresh = false)
        return localDataSource.getMovieList(listId)?.name.orEmpty()
    }

    override suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> {
        refreshMovieListCacheIfNecessary(forceRefresh = false)
        return buildPagedMovieListResponse(pageNumber)
    }

    private suspend fun buildPagedMovieListResponse(pageNumber: Int): PagedFetchResponse<MovieList> {
        val allLists = localDataSource.getAllUserLists()
        val itemsPerPage = 20
        val startIndex = (pageNumber - 1) * itemsPerPage
        val endIndex = minOf(startIndex + itemsPerPage, allLists.size)

        val items = if (startIndex < allLists.size) {
            allLists.subList(startIndex, endIndex).map { it.toEntity() }
        } else {
            emptyList()
        }

        return PagedFetchResponse(
            currentPage = pageNumber,
            items = items,
            totalPages = (allLists.size + itemsPerPage - 1) / itemsPerPage,
            totalItems = allLists.size
        )
    }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean {
        addMovieRemotely(listId, movieId)
        updateLocalCacheAfterAddingMovie(listId, movieId)
        return true
    }

    private suspend fun addMovieRemotely(listId: Int, movieId: Int) {
        remoteDataSource.addMovieToList(
            listId = listId,
            movieId = movieId,
            sessionId = authenticationPreferences.getSessionId()
        ).getOrThrow()
    }

    private suspend fun updateLocalCacheAfterAddingMovie(listId: Int, movieId: Int) {
        localDataSource.addMovieToListCache(movieId, listId)

        localDataSource.getMovieList(listId)?.let { list ->
            localDataSource.addMovieListCache(
                list.copy(itemCount = list.itemCount + 1)
            )
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
        removeMovieRemotely(listId, movieId)
        updateLocalCacheAfterRemovingMovie(listId, movieId)
        return true
    }

    private suspend fun removeMovieRemotely(listId: Int, movieId: Int) {
        remoteDataSource.removeMovieFromList(
            listId = listId,
            movieId = movieId,
            sessionId = authenticationPreferences.getSessionId()
        ).getOrThrow()
    }

    private suspend fun updateLocalCacheAfterRemovingMovie(listId: Int, movieId: Int) {
        localDataSource.removeMovieFromListCache(movieId, listId)

        localDataSource.getMovieList(listId)?.let { list ->
            localDataSource.addMovieListCache(
                list.copy(itemCount = maxOf(0, list.itemCount - 1))
            )
        }
    }

    override suspend fun refreshMovieListCache() {
        syncMutex.withLock {
            runCatching {
                val (lists, memberships) = fetchAllListsAndMemberships()
                cacheListsAndMemberships(lists, memberships)
                localDataSource.markCacheRefreshed(true)
            }.onFailure {
                localDataSource.markCacheRefreshed(false)
                crashReporter.logException(it)
                throw it
            }
        }
    }

    private suspend fun fetchAllListsAndMemberships(): Pair<List<MovieListLocal>, List<MovieListMembershipLocal>> {
        val lists = mutableListOf<MovieListLocal>()
        val memberships = mutableListOf<MovieListMembershipLocal>()

        var page = 1
        do {
            val response = remoteDataSource.getAllMovieLists(
                page = page,
                sessionId = authenticationPreferences.getSessionId()
            ).getOrThrow()

            lists.addAll(response.items.map { it.toLocal() })
            memberships.addAll(fetchMembershipsForListsPage(response.items))

            page++
        } while (page <= response.totalPages)

        return Pair(lists, memberships)
    }

    private suspend fun fetchMembershipsForListsPage(
        listsPage: List<CustomMovieListResponse>
    ): List<MovieListMembershipLocal> {
        val memberships = mutableListOf<MovieListMembershipLocal>()

        for (list in listsPage) {
            if (list.id == null) continue
            memberships.addAll(fetchAllMembershipsForList(list.id))
        }

        return memberships
    }

    private suspend fun fetchAllMembershipsForList(listId: Int): List<MovieListMembershipLocal> {
        val memberships = mutableListOf<MovieListMembershipLocal>()
        var moviePage = 1

        do {
            val listDetailsResponse = remoteDataSource.getDetails(
                listId = listId,
                page = moviePage
            ).getOrThrow()

            val items = listDetailsResponse.items.orEmpty()

            for (movie in items) {
                if (movie.id != null) {
                    memberships.add(
                        MovieListMembershipLocal(
                            movieId = movie.id,
                            listId = listId
                        )
                    )
                }
            }

            moviePage++
        } while (moviePage <= MAX_PAGES && items.isNotEmpty())

        return memberships
    }

    private suspend fun cacheListsAndMemberships(
        lists: List<MovieListLocal>,
        memberships: List<MovieListMembershipLocal>
    ) {
        localDataSource.cacheMovieListsMetadata(lists)
        localDataSource.cacheMovieListMemberships(memberships)
    }

    private suspend fun refreshMovieListCacheIfNecessary(forceRefresh: Boolean) {
        if (forceRefresh || localDataSource.shouldRefreshCache()) {
            refreshMovieListCache()
        }
    }

    private companion object {
        const val MAX_PAGES = 10
    }
}
