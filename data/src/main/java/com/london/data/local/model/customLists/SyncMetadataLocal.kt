package com.london.data.local.model.customLists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadataLocal(
    @PrimaryKey
    val syncKey: String,
    val lastSyncTime: Long,
    val isSuccess: Boolean
)
