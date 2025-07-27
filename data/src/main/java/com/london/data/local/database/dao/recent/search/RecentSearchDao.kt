package com.london.data.local.database.dao.recent.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.london.data.local.database.dao.recent.RecentDao
import com.london.data.local.model.recent.search.RecentSearchLocal

@Dao
interface RecentSearchDao : RecentDao<RecentSearchLocal> {

    @Query("SELECT * FROM recent_search_table")
    override suspend fun getAll(): List<RecentSearchLocal>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentSearchLocal)

    @Query("SELECT * FROM recent_search_table ORDER BY date DESC LIMIT 10")
    override suspend fun getRecentTen(): List<RecentSearchLocal>

    @Query("DELETE FROM recent_search_table WHERE id NOT IN (SELECT id FROM recent_search_table ORDER BY date DESC LIMIT 10)")
    override suspend fun clearOlderThanTen()

    @Query("DELETE FROM recent_search_table")
    override suspend fun clearAll()

    @Transaction
    override suspend fun insertAndKeepLastTen(item: RecentSearchLocal) {
        insert(item)
        clearOlderThanTen()
    }

    @Delete
    suspend fun delete(item: RecentSearchLocal)
}
