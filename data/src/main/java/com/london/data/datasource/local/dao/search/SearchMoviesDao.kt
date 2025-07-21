package com.london.data.datasource.local.dao.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.datasource.local.dao.SearchDao
import com.london.data.datasource.local.model.SearchMoviesLocal

@Dao
interface SearchMoviesDao : SearchDao<SearchMoviesLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(search: SearchMoviesLocal)

    @Update
    override suspend fun update(search: SearchMoviesLocal)

    @Delete
    override suspend fun delete(search: SearchMoviesLocal)

    @Query("SELECT * FROM search_movies_table")
    override suspend fun getAll(): List<SearchMoviesLocal>

    @Query("SELECT * FROM search_movies_table WHERE date = :date")
    override suspend fun getCurrentSearchByDate(date: Long): SearchMoviesLocal

    @Query("SELECT * FROM search_movies_table WHERE `query` = :query")
    override suspend fun getSearchByQuery(query: String): SearchMoviesLocal

    @Query("SELECT * FROM search_movies_table WHERE `query` = :query AND page = :page")
    override suspend fun getSearchByQueryAndPage(query: String, page: Int): SearchMoviesLocal
}