package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal

@Dao
interface SearchTvShowDao : SearchDao<SearchTvShowsResponseLocal> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchTvShowsResponseLocal)

    @Update
    override suspend fun update(search: SearchTvShowsResponseLocal)

    @Delete
    override suspend fun delete(search: SearchTvShowsResponseLocal)

    @Query("SELECT * FROM search_tv_shows_table")
    override suspend fun getAll(): SearchTvShowsResponseLocal

    @Query("SELECT * FROM search_tv_shows_table WHERE date = :date")
    override suspend fun getCurrentSearch(date: Long): SearchTvShowsResponseLocal

    @Query("SELECT * FROM search_tv_shows_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchTvShowsResponseLocal
}