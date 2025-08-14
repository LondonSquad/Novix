package com.london.data.local.database.dao.customLists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.london.data.local.model.customLists.MovieListMembershipLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ListMembershipDao {

    @Query("SELECT EXISTS(SELECT 1 FROM movie_list_membership WHERE movieId = :movieId)")
    suspend fun isMovieListed(movieId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM movie_list_membership WHERE movieId = :movieId)")
    fun isMovieListedFlow(movieId: Int): Flow<Boolean>

    @Query("SELECT listId FROM movie_list_membership WHERE movieId = :movieId")
    suspend fun getMovieListIds(movieId: Int): List<Int>

    @Query("SELECT listId FROM movie_list_membership WHERE movieId = :movieId")
    fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>>

    @Query("SELECT DISTINCT movieId FROM movie_list_membership")
    suspend fun getAllListedMovieIds(): List<Int>

    @Query("SELECT DISTINCT movieId FROM movie_list_membership")
    fun getAllListedMovieIdsFlow(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberships(memberships: List<MovieListMembershipLocal>)

    @Query("DELETE FROM movie_list_membership WHERE movieId = :movieId AND listId = :listId")
    suspend fun removeMembership(movieId: Int, listId: Int)

    @Query("DELETE FROM movie_list_membership WHERE movieId = :movieId")
    suspend fun removeAllMembershipsForMovie(movieId: Int)

    @Query("DELETE FROM movie_list_membership WHERE listId = :listId")
    suspend fun removeAllMembershipsForList(listId: Int)

    @Transaction
    suspend fun replaceAllMemberships(memberships: List<MovieListMembershipLocal>) {
        clearAll()
        insertMemberships(memberships)
    }

    @Query("DELETE FROM movie_list_membership")
    suspend fun clearAll()
}
