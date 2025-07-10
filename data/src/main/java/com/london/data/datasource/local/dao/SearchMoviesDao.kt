package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.dto.search.SearchMoviesResponse

@Dao
interface SearchMoviesDao : SearchDao<SearchMoviesResponse> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(search: SearchMoviesResponse)

    @Update
    override fun update(search: SearchMoviesResponse)

    @Delete
    override fun delete(search: SearchMoviesResponse)

    @Query("SELECT * FROM search_movies_table")
    override fun getAll(): SearchMoviesResponse

    @Query("SELECT * FROM search_movies_table WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchMoviesResponse

    @Query("SELECT * FROM search_movies_table WHERE `query` = :query")
    override fun getSearchByQuery(query: String): SearchMoviesResponse
}