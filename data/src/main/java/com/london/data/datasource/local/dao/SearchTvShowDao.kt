package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.dto.search.SearchTvShowsResponse

@Dao
interface SearchTvShowDao : SearchDao<SearchTvShowsResponse> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(search: SearchTvShowsResponse)

    @Update
    override fun update(search: SearchTvShowsResponse)

    @Delete
    override fun delete(search: SearchTvShowsResponse)

    @Query("SELECT * FROM search_tv_shows_table")
    override fun getAll(): SearchTvShowsResponse

    @Query("SELECT * FROM search_tv_shows_table WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchTvShowsResponse

    @Query("SELECT * FROM search_tv_shows_table WHERE `query` = :query")
    override fun getSearchByQuery(query: String): SearchTvShowsResponse
}