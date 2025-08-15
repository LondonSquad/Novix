package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageRatingUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val movieRepository: MovieRepository,
) {

   private suspend fun getAllRatedMedia(): List<RatedMedia> =
        buildList {
            addAll(movieRepository.getAllRatedMovies())
            addAll(tvShowRepository.getAllRatedTvShows())
        }

    suspend fun getAllRatedMediaSorted(): List<RatedMedia> =
        getAllRatedMedia().sortedByDescending { it.rating }
    suspend fun getRatedMediaSorted(): List<RatedMedia> =
        getRatedMedia().sortedByDescending { it.rating }

    suspend fun getAllRatedMovies(): List<RatedMedia> =
        movieRepository.getAllRatedMovies()

    suspend fun getAllRatedTvShows(): List<RatedMedia> =
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

    suspend fun addTvShowRatingById(id: Int, rating: Int) : Boolean =
        tvShowRepository.addTvShowById(id = id, rating = rating)

    suspend fun getRateAccountMovieStatesById(id: Int): Int =
        movieRepository.getAccountMovieStatesById(id = id).rate

    suspend fun getRateAccountTvShowState(tvShowId: Int) : Int=
        tvShowRepository.getAccountTvShowState(tvShowId = tvShowId).rate

    suspend fun deleteMovieRating(movieId: Int) : Boolean =
    suspend fun deleteMovieRating(movieId: Int) =
        movieRepository.deleteMovieRating(movieId = movieId)

    suspend fun deleteTvShowRating(tvShowId: Int) : Boolean =
        tvShowRepository.deleteTvShowRating(tvShowId = tvShowId)

    suspend fun addTvEpisodeRatingById(
        id: Int,
        rating: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) : Boolean = tvShowRepository.addTvShowEpisode(
        tvShowId = id,
        rating = rating,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
    )

    suspend fun getRateAccountTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) : Int = tvShowRepository.getAccountTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    ).rate
    suspend fun deleteTvShowRating(tvShowId: Int) =
        tvShowRepository.deleteTvShowRating(id = tvShowId)

    private suspend fun getRatedMedia(): List<RatedMedia> =
        buildList {
            addAll(movieRepository.getAllRatedMovies())
            addAll(tvShowRepository.getAllRatedTvShows())
        }
}
