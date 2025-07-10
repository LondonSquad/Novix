package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal

@Dao
interface SearchTvShowDao : SearchDao<SearchTvShowsResponseLocal> {
    @Insert
    override fun insert(search: SearchTvShowsResponseLocal)

    @Update
    override fun update(search: SearchTvShowsResponseLocal)

    @Delete
    override fun delete(search: SearchTvShowsResponseLocal)

    @Query("SELECT * FROM search_tv_shows_response")
    override fun getAll(): SearchTvShowsResponseLocal

    @Query("SELECT * FROM search_tv_shows_response WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchTvShowsResponseLocal
}