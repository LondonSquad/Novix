package com.london.data.local.model.home.topRated

import androidx.room.Entity
import com.london.data.mapper.genre.GenreMapper
import com.london.domain.entity.shared.MediaType

@Entity(
    tableName = "top_rated_table",
    primaryKeys = ["id", "mediaType"]
)
data class TopRatedLocal(
    val id: Int,
    val name: String,
    val posterPictureUrl: String,
    val mediaType: MediaType,
    val date: Long = System.currentTimeMillis(),
    val genre: List<Int>
) : GenreMapper
