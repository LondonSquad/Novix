package com.london.data.local.database.dao.customLists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.model.customLists.SyncMetadataLocal

@Dao
interface SyncMetadataDao {

    @Query("SELECT * FROM sync_metadata WHERE syncKey = :syncKey")
    suspend fun getSyncMetadata(syncKey: String): SyncMetadataLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetadataLocal)

    companion object {
        const val MOVIE_LISTS_SYNC_KEY = "movie_lists_sync"
    }
}