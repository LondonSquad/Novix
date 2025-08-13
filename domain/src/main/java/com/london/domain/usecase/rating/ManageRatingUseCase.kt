package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageRatingUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val movieRepository: MovieRepository,
) {
    suspend fun getRatedMediaSorted(): List<RatedMedia> =
        getRatedMedia().sortedByDescending { it.rating }

    suspend fun getRatedMovies(): List<RatedMedia> =
        movieRepository.getAllRatedMovies()

    suspend fun getRatedTvShows(): List<RatedMedia> =
        tvShowRepository.getAllRatedTvShows()

    suspend fun getRateAccountMovieStatesById(id: Int): Int =
        movieRepository.getAccountMovieStatesById(id = id).rate

    suspend fun getRateAccountTvShowStatesById(id: Int) =
        tvShowRepository.getAccountTvShowStateById(id = id).rate

    suspend fun getRatedAccountTvShowEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = tvShowRepository.getAccountTvEpisode(
        id = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    ).rate

    suspend fun addTvShowEpisodeRatingById(
        id: Int,
        rating: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = tvShowRepository.addTvShowEpisode(
        id = id,
        rating = rating,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
    )

    suspend fun addMovieRatingById(id: Int, rating: Int): Boolean =
        movieRepository.addMovieRatingById(id = id, rating = rating)

    suspend fun addTvShowRatingById(id: Int, rating: Int) =
        tvShowRepository.addTvShowById(id = id, rating = rating)

    suspend fun deleteMovieRating(movieId: Int) =
        movieRepository.deleteMovieRating(movieId = movieId)

    suspend fun deleteTvShowRating(tvShowId: Int) =
        tvShowRepository.deleteTvShowRating(id = tvShowId)

    private suspend fun getRatedMedia(): List<RatedMedia> =
        buildList {
            addAll(movieRepository.getAllRatedMovies())
            addAll(tvShowRepository.getAllRatedTvShows())
        }
}