package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.RatingRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageRatingUseCase @Inject constructor(
    private val repository: RatingRepository,
    private val tvShowRepository: TvShowRepository,
    private val ratingRepository: RatingRepository,
    private val movieRepository: MovieDetailsRepository,
) {

    suspend fun getRatedMedia(): List<RatedMedia> =
        buildList {
            addAll(repository.getAllRatedMovies())
            addAll(repository.getAllRatedTvShows())
        }

    suspend fun getRatedMediaSorted(): List<RatedMedia> =
        getRatedMedia().sortedByDescending { it.rating }

    suspend fun getRatedMovies(): List<RatedMedia> =
        repository.getAllRatedMovies()

    suspend fun getRatedTvShows(): List<RatedMedia> =
        repository.getAllRatedTvShows()

    suspend fun getRateAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = tvShowRepository.getAccountTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    ).rate

    suspend fun addTvEpisodeRatingById(
        id: Int,
        rating: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = ratingRepository.addTvEpisode(
        tvShowId = id,
        rating = rating,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
    )

    suspend fun addMovieRatingById(id: Int, rating: Int): Boolean =
        repository.addMovieRatingById(id = id, rating = rating)

    suspend fun addTvShowRatingById(id: Int, rating: Int) =
        ratingRepository.addTvShowById(id = id, rating = rating)

    suspend fun getRateAccountMovieStatesById(id: Int): Int =
        movieRepository.getAccountMovieStatesById(id = id).rate

    suspend fun getRateAccountTvShowState(tvShowId: Int) =
        tvShowRepository.getAccountTvShowState(tvShowId = tvShowId).rate

    suspend fun deleteMovieRating(movieId: Int) =
        ratingRepository.deleteMovieRating(movieId = movieId)

    suspend fun deleteTvShowRating(tvShowId: Int) =
        ratingRepository.deleteTvShowRating(tvShowId = tvShowId)
}