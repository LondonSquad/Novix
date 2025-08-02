package com.london.data.local.database.dao.home.popular

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.model.home.popular.PopularSectionLocal

@Dao
interface PopularSectionDao {

    @Query("SELECT * FROM popular_section_table")
    suspend fun getAll(): List<PopularSectionLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PopularSectionLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PopularSectionLocal>)

    @Query("DELETE FROM popular_section_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM popular_section_table WHERE date = :date")
    suspend fun getCurrentPopularByDate(date: Long): PopularSectionLocal
}
