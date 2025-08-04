package com.london.data.local.database.dao.home.toprated

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.database.dao.home.HomeDao
import com.london.data.local.model.home.TopRatedLocal

@Dao
interface TopRatedDao: HomeDao<TopRatedLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(item: TopRatedLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAll(items: List<TopRatedLocal>)

    @Query("DELETE FROM top_rated_table")
    override suspend fun deleteAll()

    @Query("SELECT * FROM top_rated_table")
    override suspend fun getAll(): List<TopRatedLocal>

    @Query("SELECT * FROM top_rated_table WHERE date = :date")
    override suspend fun getByDate(date: Long): TopRatedLocal
    
}
