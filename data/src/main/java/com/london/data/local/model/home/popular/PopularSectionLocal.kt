package com.london.data.local.model.home.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.london.domain.entity.recent.MediaType

@Entity(tableName = "popular_section_table")
data class PopularSectionLocal(
    @PrimaryKey
    val id: Int,
    val name: String,
    val posterPictureUrl: String,
    val backdropPictureUrl: String,
    val rating: Double,
    val mediaType: MediaType,
    val date: Long = System.currentTimeMillis()
)
