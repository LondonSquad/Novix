package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchMoviesResponse

@Dao
interface SearchMoviesDao : SearchDao<SearchMoviesResponse> {

    @Insert
    override fun insert(search: SearchMoviesResponse)

    @Update
    override fun update(search: SearchMoviesResponse)

    @Delete
    override fun delete(search: SearchMoviesResponse)

    @Query("SELECT * FROM search_movies_response")
    override fun getAll(): SearchMoviesResponse

    @Query("SELECT * FROM search_movies_response WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchMoviesResponse
}