package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal

@Dao
interface SearchMoviesDao : SearchDao<SearchMoviesResponseLocal> {

    @Insert
    override fun insert(search: SearchMoviesResponseLocal)

    @Update
    override fun update(search: SearchMoviesResponseLocal)

    @Delete
    override fun delete(search: SearchMoviesResponseLocal)

    @Query("SELECT * FROM search_movies_response")
    override fun getAll(): SearchMoviesResponseLocal

    @Query("SELECT * FROM search_movies_response WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchMoviesResponseLocal
}