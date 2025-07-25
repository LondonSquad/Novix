package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre_interest")
data class GenreInterestEntity(
    @PrimaryKey(autoGenerate = false)
    val genreId: Int,
    val mediaType: String,
    val count: Int
)
