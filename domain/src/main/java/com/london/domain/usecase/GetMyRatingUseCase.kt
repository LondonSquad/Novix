package com.london.domain.usecase

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class GetMyRatingUseCase @Inject constructor(
    private val repository: MyRatingRepository
) {
    suspend fun getAllRated(): List<RatedMedia> {
        return repository.getAllRatedMedia()
            .map { media: RatedMedia ->
                RatedMedia(
                    id = media.id,
                    title = media.title,
                    posterPath = media.posterPath,
                    rating = media.rating,
                    isMovie = media.isMovie
                )
            }
            .sortedByDescending { media -> media.rating }
    }

    suspend fun getRatedMovies(): List<RatedMedia> {
        return repository.getAllRatedMedia()
            .filter { media: RatedMedia -> media.isMovie }
            .map { media: RatedMedia ->
                RatedMedia(
                    id = media.id,
                    title = media.title,
                    posterPath = media.posterPath,
                    rating = media.rating,
                    isMovie = media.isMovie
                )
            }
            .sortedByDescending { media -> media.rating }
    }

    suspend fun getRatedTvShows(): List<RatedMedia> {
        return repository.getAllRatedMedia()
            .filter { media: RatedMedia -> !media.isMovie }
            .map { media: RatedMedia ->
                RatedMedia(
                    id = media.id,
                    title = media.title,
                    posterPath = media.posterPath,
                    rating = media.rating,
                    isMovie = media.isMovie
                )
            }
            .sortedByDescending { media -> media.rating }
    }
}