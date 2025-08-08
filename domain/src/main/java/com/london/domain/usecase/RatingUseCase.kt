package com.london.domain.usecase

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.RatingRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class RatingUseCase @Inject constructor(
    private val repository: RatingRepository,
    private val tvShowRepository: TvShowRepository,
    private val ratingRepository: RatingRepository,
    private val movieRepository: MovieDetailsRepository,
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

    suspend fun getAccountTvEpisodeUseCase(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int

    ) = tvShowRepository.getAccountTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    ).rate

    suspend fun addTvEpisodeRatingByIdUseCase(
        id: Int,
        rating: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) =
        ratingRepository.addTvEpisode(
            tvShowId = id,
            rating = rating,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )

    suspend fun addMovieRatingByIdUseCase(
        id: Int,
        rating: Int,
    ): Boolean = repository.addMovieRatingById(
        id = id,
        rating = rating,
    )

    suspend fun addTvShowRatingByIdUseCase(id: Int, rating: Int) =
        ratingRepository.addTvShowById(
            id = id,
            rating = rating,
        )

    suspend fun getAccountMovieStatesById(id: Int): Int =
        movieRepository.getAccountMovieStatesById(
            id = id,
        ).rate

    suspend fun getAccountTvShowStateUseCase(
        tvShowId: Int,
    ) = tvShowRepository.getAccountTvShowState(
        tvShowId = tvShowId,
    ).rate
}