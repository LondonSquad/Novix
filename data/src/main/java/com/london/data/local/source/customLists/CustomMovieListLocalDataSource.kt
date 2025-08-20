package com.london.data.local.source.customLists

import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.model.customLists.MovieListMembershipLocal
import kotlinx.coroutines.flow.Flow

interface CustomMovieListLocalDataSource {
    suspend fun isMovieListed(movieId: Int): Boolean
    fun isMovieListedFlow(movieId: Int): Flow<Boolean>
    suspend fun getMovieListIds(movieId: Int): List<Int>
    fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>>
    suspend fun getAllListedMovieIds(): List<Int>
    fun getAllListedMovieIdsFlow(): Flow<List<Int>>
    suspend fun getMovieIdsForList(listId: Int, limit: Int, offset: Int): List<Int>
    fun getMovieIdsForListFlow(listId: Int): Flow<List<Int>>
    suspend fun getMovieCountForList(listId: Int): Int
    fun getMovieCountForListFlow(listId: Int): Flow<Int>
    suspend fun getAllUserLists(): List<MovieListLocal>
    fun getAllUserListsFlow(): Flow<List<MovieListLocal>>
    suspend fun getMovieList(listId: Int): MovieListLocal?
    fun getMovieListFlow(listId: Int): Flow<MovieListLocal?>
    suspend fun getMovieListsPaged(limit: Int, offset: Int): List<MovieListLocal>
    suspend fun cacheMovieListMemberships(memberships: List<MovieListMembershipLocal>)
    suspend fun addMovieToListCache(movieId: Int, listId: Int)
    suspend fun removeMovieFromListCache(movieId: Int, listId: Int)
    suspend fun replaceMembershipsForList(listId: Int, memberships: List<MovieListMembershipLocal>)
    suspend fun cacheMovieListsMetadata(lists: List<MovieListLocal>)
    suspend fun addMovieListCache(movieList: MovieListLocal)
    suspend fun removeMovieListCache(listId: Int)
    suspend fun updateMovieListItemCount(listId: Int, itemCount: Int)
    suspend fun shouldRefreshCache(): Boolean
    suspend fun markCacheRefreshed(success: Boolean)
    suspend fun clearAllCache()
}
