package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class GetMyRating @Inject constructor(
    private val repository: MyRatingRepository
) {
    suspend fun getAllRated(): List<RatedMedia> {
        val ratedMedia = repository.getAllRatedMedia()
        return ratedMedia.map { media ->
            RatedMedia(
                id = media.id,
                title = media.title,
                posterPath = media.posterPath,
                rating = media.rating,
                isMovie = media.isMovie,
                addedAt = System.currentTimeMillis()
            )
        }.sortedByDescending { it.addedAt }
    }

    suspend fun getRatedMovies(): List<RatedMedia> {
        return repository.getAllRatedMedia()
            .filter { it.isMovie }
            .map { media ->
                RatedMedia(
                    id = media.id,
                    title = media.title,
                    posterPath = media.posterPath,
                    rating = media.rating,
                    isMovie = media.isMovie,
                    addedAt = System.currentTimeMillis()
                )
            }
            .sortedByDescending { it.addedAt }
    }

    suspend fun getRatedTvShows(): List<RatedMedia> {
        return repository.getAllRatedMedia()
            .filter { !it.isMovie }
            .map { media ->
                RatedMedia(
                    id = media.id,
                    title = media.title,
                    posterPath = media.posterPath,
                    rating = media.rating,
                    isMovie = media.isMovie,
                    addedAt = System.currentTimeMillis()
                )
            }
            .sortedByDescending { it.addedAt }
    }
}