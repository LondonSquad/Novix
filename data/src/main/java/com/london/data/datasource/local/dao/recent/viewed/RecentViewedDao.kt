@file:KoverIgnore
package com.london.data.datasource.local.dao.recent.viewed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.london.data.datasource.local.dao.recent.RecentDao
import com.london.data.datasource.local.model.recent.RecentViewedLocal
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
}
