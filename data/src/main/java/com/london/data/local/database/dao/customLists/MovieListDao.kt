package com.london.data.local.database.dao.customLists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.local.model.customLists.MovieListLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDao {

    @Query("SELECT * FROM cached_movie_lists ORDER BY cachedAt DESC")
    suspend fun getAllLists(): List<MovieListLocal>

    @Query("SELECT * FROM cached_movie_lists ORDER BY cachedAt DESC")
    fun getAllListsFlow(): Flow<List<MovieListLocal>>

    @Query("SELECT * FROM cached_movie_lists WHERE id = :listId")
    suspend fun getList(listId: Int): MovieListLocal?

    @Query("SELECT * FROM cached_movie_lists WHERE id = :listId")
    fun getListFlow(listId: Int): Flow<MovieListLocal?>

    // Pagination support
    @Query("SELECT * FROM cached_movie_lists ORDER BY cachedAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getListsPaged(limit: Int, offset: Int): List<MovieListLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<MovieListLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MovieListLocal)

    @Update
    suspend fun updateList(list: MovieListLocal)

    @Query("UPDATE cached_movie_lists SET itemCount = :itemCount WHERE id = :listId")
    suspend fun updateItemCount(listId: Int, itemCount: Int)

    @Query("DELETE FROM cached_movie_lists WHERE id = :listId")
    suspend fun removeList(listId: Int)

    @Query("DELETE FROM cached_movie_lists")
    suspend fun clearAll()
}

