package com.london.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.model.SearchActorsLocal

@Dao
interface SearchActorsDao : SearchDao<SearchActorsLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchActorsLocal)

    @Update
    override suspend fun update(search: SearchActorsLocal)

    @Delete
    override suspend fun delete(search: SearchActorsLocal)

    @Query("SELECT * FROM search_actors_table")
    override suspend fun getAll(): List<SearchActorsLocal>

    @Query("SELECT * FROM search_actors_table WHERE date = :date")
    override suspend fun getCurrentSearchByDate(date: Long): SearchActorsLocal

    @Query("SELECT * FROM search_actors_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchActorsLocal
}