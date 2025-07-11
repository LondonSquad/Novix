package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.model.SearchTvShowLocal

@Dao
interface SearchTvShowDao : SearchDao<SearchTvShowLocal> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchTvShowLocal)

    @Update
    override suspend fun update(search: SearchTvShowLocal)

    @Delete
    override suspend fun delete(search: SearchTvShowLocal)

    @Query("SELECT * FROM search_tv_shows_table")
    override suspend fun getAll(): SearchTvShowLocal

    @Query("SELECT * FROM search_tv_shows_table WHERE date = :date")
    override suspend fun getCurrentSearchByDate(date: Long): SearchTvShowLocal

    @Query("SELECT * FROM search_tv_shows_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchTvShowLocal
}