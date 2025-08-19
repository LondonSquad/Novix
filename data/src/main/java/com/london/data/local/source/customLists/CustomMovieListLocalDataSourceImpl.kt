package com.london.data.local.source.customLists

import com.london.data.local.database.dao.customLists.ListMembershipDao
import com.london.data.local.database.dao.customLists.MovieListDao
import com.london.data.local.database.dao.customLists.SyncMetadataDao
import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.model.customLists.MovieListMembershipLocal
import com.london.data.local.model.customLists.SyncMetadataLocal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomMovieListLocalDataSourceImpl @Inject constructor(
    private val membershipDao: ListMembershipDao,
    private val movieListDao: MovieListDao,
    private val syncMetadataDao: SyncMetadataDao
) : CustomMovieListLocalDataSource {

    override suspend fun getAllUserLists(): List<MovieListLocal> = movieListDao.getAllLists()

    override suspend fun getAllListedMovieIds(): List<Int> = membershipDao.getAllListedMovieIds()

    override fun getAllUserListsFlow(): Flow<List<MovieListLocal>> = movieListDao.getAllListsFlow()

    override fun getAllListedMovieIdsFlow(): Flow<List<Int>> = membershipDao.getAllListedMovieIdsFlow()

    override suspend fun isMovieListed(movieId: Int): Boolean = membershipDao.isMovieListed(movieId = movieId)

    override fun isMovieListedFlow(movieId: Int): Flow<Boolean> =
        membershipDao.isMovieListedFlow(movieId = movieId)

    override suspend fun getMovieListIds(movieId: Int): List<Int> =
        membershipDao.getMovieListIds(movieId = movieId)

    override fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>> =
        membershipDao.getMovieListIdsFlow(movieId = movieId)

    override suspend fun getMovieIdsForList(listId: Int, limit: Int, offset: Int): List<Int> =
        membershipDao.getMovieIdsForList(listId = listId, limit =  limit, offset = offset)

    override fun getMovieIdsForListFlow(listId: Int): Flow<List<Int>> =
        membershipDao.getMovieIdsForListFlow(listId = listId)

    override suspend fun getMovieCountForList(listId: Int): Int =
        membershipDao.getMovieCountForList(listId = listId)

    override fun getMovieCountForListFlow(listId: Int): Flow<Int> =
        membershipDao.getMovieCountForListFlow(listId = listId)

    override suspend fun getMovieList(listId: Int): MovieListLocal? = movieListDao.getList(listId = listId)

    override fun getMovieListFlow(listId: Int): Flow<MovieListLocal?> =
        movieListDao.getListFlow(listId = listId)

    override suspend fun getMovieListsPaged(limit: Int, offset: Int): List<MovieListLocal> =
        movieListDao.getListsPaged(limit = limit, offset = offset)

    override suspend fun cacheMovieListMemberships(memberships: List<MovieListMembershipLocal>) =
        membershipDao.replaceAllMemberships(memberships)

    override suspend fun addMovieToListCache(movieId: Int, listId: Int) = membershipDao.insertMembership(
        MovieListMembershipLocal(movieId = movieId, listId = listId)
    )

    override suspend fun removeMovieFromListCache(movieId: Int, listId: Int) =
        membershipDao.removeMembership(movieId = movieId, listId = listId)

    override suspend fun replaceMembershipsForList(
        listId: Int,
        memberships: List<MovieListMembershipLocal>
    ) = membershipDao.replaceMembershipsForList(listId = listId, memberships = memberships)

    override suspend fun cacheMovieListsMetadata(lists: List<MovieListLocal>) =
        movieListDao.insertLists(lists)

    override suspend fun addMovieListCache(movieList: MovieListLocal) =
        movieListDao.insertList(list = movieList)

    override suspend fun removeMovieListCache(listId: Int) {
        membershipDao.removeAllMembershipsForList(listId)
        movieListDao.removeList(listId)
    }

    override suspend fun updateMovieListItemCount(listId: Int, itemCount: Int) =
        movieListDao.updateItemCount(listId = listId, itemCount = itemCount)

    override suspend fun shouldRefreshCache(): Boolean {
        val metadata = syncMetadataDao.getSyncMetadata(SYNC_KEY)
        return metadata == null ||
            !metadata.isSuccess ||
            (System.currentTimeMillis() - metadata.lastSyncTime) > CACHE_VALIDITY_MS
    }

    override suspend fun markCacheRefreshed(success: Boolean) {
        syncMetadataDao.insertSyncMetadata(
            SyncMetadataLocal(
                syncKey = SYNC_KEY,
                lastSyncTime = System.currentTimeMillis(),
                isSuccess = success
            )
        )
    }

    override suspend fun clearAllCache() {
        membershipDao.clearAll()
        movieListDao.clearAll()
    }

    private companion object {
        const val CACHE_VALIDITY_MS = 30 * 60 * 1000L // 30 minutes
        const val SYNC_KEY = SyncMetadataDao.MOVIE_LISTS_SYNC_KEY
    }
}
