@file:KoverIgnore

package com.london.data.local.database.dao.recent.viewed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.london.data.local.database.dao.recent.RecentDao
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.domain.KoverIgnore

@Dao
interface RecentViewedDao : RecentDao<RecentViewedLocal> {

    @Query("SELECT * FROM recent_viewed_table")
    override suspend fun getAll(): List<RecentViewedLocal>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentViewedLocal)
    
    @Query("SELECT * FROM recent_viewed_table ORDER BY viewDate DESC LIMIT 10")
    override suspend fun getRecentTen(): List<RecentViewedLocal>

    @Query("DELETE FROM recent_viewed_table WHERE id NOT IN (SELECT id FROM recent_viewed_table ORDER BY viewDate DESC LIMIT 10)")
    override suspend fun clearOlderThanTen()

    @Query("DELETE FROM recent_viewed_table")
    override suspend fun clearAll()

    @Transaction
    override suspend fun insertAndKeepLastTen(item: RecentViewedLocal) {
        insert(item)
        clearOlderThanTen()
    }

    @Delete
    suspend fun delete(item: RecentViewedLocal)
}
