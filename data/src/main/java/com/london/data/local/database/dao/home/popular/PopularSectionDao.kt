package com.london.data.local.database.dao.home.popular

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.model.home.popular.PopularSectionLocal

@Dao
interface PopularSectionDao: HomeDao<PopularSectionLocal> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(item: PopularSectionLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAll(items: List<PopularSectionLocal>)

    @Query("DELETE FROM popular_section_table")
    override suspend fun deleteAll()

    @Query("SELECT * FROM popular_section_table")
    override suspend fun getAll(): List<PopularSectionLocal>

    @Query("SELECT * FROM popular_section_table WHERE date = :date")
    override suspend fun getByDate(date: Long): PopularSectionLocal
}
