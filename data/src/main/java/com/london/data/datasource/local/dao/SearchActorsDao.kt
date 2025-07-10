package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.london.data.dto.search.SearchActorsResponse

@Dao
interface SearchActorsDao : SearchDao<SearchActorsResponse> {

    @Insert
    override fun insert(search: SearchActorsResponse)

    @Update
    override fun update(search: SearchActorsResponse)

    @Delete
    override fun delete(search: SearchActorsResponse)

    @Query("SELECT * FROM search_actors_table")
    override fun getAll(): SearchActorsResponse

    @Query("SELECT * FROM search_actors_table WHERE date = :date")
    override fun getCurrentSearch(date: Long): SearchActorsResponse
}