package com.london.data.local.database.dao.home.upcoming

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.model.home.upcoming.UpComingSectionLocal

@Dao
interface UpcomingSectionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UpComingSectionLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UpComingSectionLocal>)

    @Query("DELETE FROM upcoming_section_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM upcoming_section_table")
    suspend fun getAll(): List<UpComingSectionLocal>

    @Query("SELECT * FROM upcoming_section_table WHERE date = :date")
    suspend fun getByDate(date: Long): UpComingSectionLocal

    @Query("SELECT * FROM upcoming_section_table WHERE categoryId = :categoryId AND page = :page")
    suspend fun getUpComingMoviesPage(categoryId: Int?, page: Int): UpComingSectionLocal
}