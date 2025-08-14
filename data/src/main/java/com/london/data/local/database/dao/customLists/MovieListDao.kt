package com.london.data.local.database.dao.customLists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.model.customLists.MovieListLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDao {

    @Query("SELECT * FROM cached_movie_lists")
    suspend fun getAllLists(): List<MovieListLocal>

    @Query("SELECT * FROM cached_movie_lists")
    fun getAllListsFlow(): Flow<List<MovieListLocal>>

    @Query("SELECT * FROM cached_movie_lists WHERE id = :listId")
    suspend fun getList(listId: Int): MovieListLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<MovieListLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MovieListLocal)

    @Query("DELETE FROM cached_movie_lists WHERE id = :listId")
    suspend fun removeList(listId: Int)

    @Query("DELETE FROM cached_movie_lists")
    suspend fun clearAll()
}

