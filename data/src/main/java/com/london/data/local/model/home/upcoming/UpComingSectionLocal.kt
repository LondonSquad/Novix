package com.london.data.local.model.home.upcoming

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upcoming_section_table")
data class UpComingSectionLocal(
    val date: Long = System.currentTimeMillis(),
    @PrimaryKey
    val categoryId: Int,
    val page: Int,
    val results: List<UpComingMovieDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class UpComingMovieDtoLocal(
    val id: Int,
    val imageUrl: String,
    val genreIds: List<Int>,
)
