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
    suspend fun getAllUserLists(): List<MovieListLocal>
    fun getAllUserListsFlow(): Flow<List<MovieListLocal>>
    suspend fun cacheMovieListMemberships(memberships: List<MovieListMembershipLocal>)
    suspend fun addMovieToListCache(movieId: Int, listId: Int)
    suspend fun removeMovieFromListCache(movieId: Int, listId: Int)
    suspend fun removeMovieListCache(listId: Int)
    suspend fun addMovieListCache(movieList: MovieListLocal)
    suspend fun shouldRefreshCache(): Boolean
    suspend fun markCacheRefreshed(success: Boolean)
    suspend fun clearAllCache()
}