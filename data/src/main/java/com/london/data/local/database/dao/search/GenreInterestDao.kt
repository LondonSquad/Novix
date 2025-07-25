package com.london.data.local.database.dao.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.london.data.local.model.search.GenreInterestEntity

@Dao
interface GenreInterestDao {

    @Query("SELECT * FROM genre_interest WHERE mediaType = :mediaType ORDER BY count DESC")
    suspend fun getGenresByInterest(mediaType: String): List<GenreInterestEntity>

    @Query("SELECT * FROM genre_interest WHERE genreId = :genreId AND mediaType = :mediaType LIMIT 1")
    suspend fun getGenreInterest(genreId: Int, mediaType: String): GenreInterestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreInterest(genreInterest: GenreInterestEntity)

    @Update
    suspend fun updateGenreInterest(genreInterest: GenreInterestEntity)
}
