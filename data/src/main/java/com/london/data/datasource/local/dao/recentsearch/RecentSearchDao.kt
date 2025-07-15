package com.london.data.datasource.local.dao.recentsearch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.london.data.datasource.local.model.RecentSearch

@Dao
interface RecentSearchDao : RecentDao<RecentSearch> {
    @Query("SELECT * FROM recent_search_table")
    override suspend fun getAll(): List<RecentSearch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(item: RecentSearch)

    @Query("SELECT * FROM recent_search_table ORDER BY date DESC LIMIT 10")
    override suspend fun getRecentTen(): List<RecentSearch>

    @Query("DELETE FROM recent_search_table WHERE id NOT IN (SELECT id FROM recent_search_table ORDER BY date DESC LIMIT 10)")
    override suspend fun clearOlderThanTen()

    @Query("DELETE FROM recent_search_table")
    override suspend fun clearAll()

    @Transaction
    override suspend fun insertAndKeepLastTen(item: RecentSearch) {
        insert(item)
        clearOlderThanTen()
    }
}