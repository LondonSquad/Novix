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

    override suspend fun isMovieListed(movieId: Int): Boolean {
        return membershipDao.isMovieListed(movieId)
    }

    override fun isMovieListedFlow(movieId: Int): Flow<Boolean> {
        return membershipDao.isMovieListedFlow(movieId)
    }

    override suspend fun getMovieListIds(movieId: Int): List<Int> {
        return membershipDao.getMovieListIds(movieId)
    }

    override fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>> {
        return membershipDao.getMovieListIdsFlow(movieId)
    }

    override suspend fun getAllListedMovieIds(): List<Int> {
        return membershipDao.getAllListedMovieIds()
    }

    override fun getAllListedMovieIdsFlow(): Flow<List<Int>> {
        return membershipDao.getAllListedMovieIdsFlow()
    }

    override suspend fun getAllUserLists(): List<MovieListLocal> {
        return movieListDao.getAllLists()
    }

    override fun getAllUserListsFlow(): Flow<List<MovieListLocal>> {
        return movieListDao.getAllListsFlow()
    }

    override suspend fun cacheMovieListMemberships(memberships: List<MovieListMembershipLocal>) {
        membershipDao.replaceAllMemberships(memberships)
    }

    override suspend fun addMovieToListCache(movieId: Int, listId: Int) {
        membershipDao.insertMemberships(
            listOf(MovieListMembershipLocal(movieId = movieId, listId = listId))
        )
    }

    override suspend fun removeMovieListCache(listId: Int) {
        membershipDao.removeAllMembershipsForList(listId)
        movieListDao.removeList(listId)
    }

    override suspend fun addMovieListCache(movieList: MovieListLocal) {
        movieListDao.insertList(movieList)
    }

    override suspend fun removeMovieFromListCache(movieId: Int, listId: Int) {
        membershipDao.removeMembership(movieId, listId)
    }

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

    companion object {
        private const val CACHE_VALIDITY_MS = 30 * 60 * 1000L // 30 minutes
        private const val SYNC_KEY = SyncMetadataDao.MOVIE_LISTS_SYNC_KEY
    }

}