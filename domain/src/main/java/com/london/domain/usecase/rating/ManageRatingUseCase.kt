package com.london.domain.usecase.rating

import com.london.domain.entity.shared.RatedMedia
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageRatingUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val movieRepository: MovieRepository,
) {
    suspend fun getRatedMediaById(id: Int): RatedMedia? =
        getRatedMedia().find { it.id == id }

    suspend fun getRatedMediaSorted(): List<RatedMedia> =
        getRatedMedia().sortedByDescending { it.rating }

    suspend fun getRateAccountMovieStatesById(id: Int): Int =
        movieRepository.getAccountMovieStatesById(id = id).rate

    suspend fun getRateAccountTvShowStatesById(id: Int) =
        tvShowRepository.getAccountTvShowStateById(id = id).rate

    suspend fun getRateAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Int = tvShowRepository.getAccountTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    ).rate

    suspend fun addMovieRatingById(id: Int, rating: Int): Boolean =
        movieRepository.addMovieRatingById(id = id, rating = rating)

    suspend fun addTvShowRatingById(id: Int, rating: Int) =
        tvShowRepository.addTvShowById(id = id, rating = rating)

    suspend fun addTvEpisodeRatingById(
        id: Int,
        rating: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Boolean = tvShowRepository.addTvShowEpisode(
        tvShowId = id,
        rating = rating,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
    )

    suspend fun deleteMovieRating(movieId: Int) =
        movieRepository.deleteMovieRating(movieId = movieId)

    suspend fun deleteTvShowRating(tvShowId: Int) =
        tvShowRepository.deleteTvShowRating(tvShowId = tvShowId)

    private suspend fun getRatedMedia(): List<RatedMedia> =
        buildList {
            addAll(movieRepository.getAllRatedMovies())
            addAll(tvShowRepository.getAllRatedTvShows())
        }
}
