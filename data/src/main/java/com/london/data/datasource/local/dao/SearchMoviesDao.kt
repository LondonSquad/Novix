package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal

@Dao
interface SearchMoviesDao : SearchDao<SearchMoviesResponseLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchMoviesResponseLocal)

    @Update
    override suspend fun update(search: SearchMoviesResponseLocal)

    @Delete
    override suspend fun delete(search: SearchMoviesResponseLocal)

    @Query("SELECT * FROM search_movies_table")
    override suspend fun getAll(): SearchMoviesResponseLocal

    @Query("SELECT * FROM search_movies_table WHERE date = :date")
    override suspend fun getCurrentSearch(date: Long): SearchMoviesResponseLocal

    @Query("SELECT * FROM search_movies_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchMoviesResponseLocal
}