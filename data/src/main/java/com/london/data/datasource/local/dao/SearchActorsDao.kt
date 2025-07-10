package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dto.SearchActorsResponseLocal

@Dao
interface SearchActorsDao : SearchDao<SearchActorsResponseLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchActorsResponseLocal)

    @Update
    override suspend fun update(search: SearchActorsResponseLocal)

    @Delete
    override suspend fun delete(search: SearchActorsResponseLocal)

    @Query("SELECT * FROM search_actors_table")
    override suspend fun getAll(): SearchActorsResponseLocal

    @Query("SELECT * FROM search_actors_table WHERE date = :date")
    override suspend fun getCurrentSearch(date: Long): SearchActorsResponseLocal

    @Query("SELECT * FROM search_actors_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchActorsResponseLocal
}