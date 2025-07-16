package com.london.data.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre_interest")
data class GenreInterestEntity(
    @PrimaryKey(autoGenerate = false)
    val genreId: Int,
    val genreType: String,
    val count: Int
)

