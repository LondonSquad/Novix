package com.london.domain.repository

import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.Trending
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.MovieImages
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia

interface MovieRepository {
    suspend fun getMovieById(id: Int): MovieDetails
    suspend fun getSimilarMoviesById(id: Int): List<Movie>
    suspend fun getMovieImagesById(id: Int): MovieImages
    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
    suspend fun getMovieVideos(movieId: Int): List<String>
    suspend fun getActorMoviePicksById(id: Int): ActorMediaDetails
    suspend fun getPopularMovies(): List<PopularMedia>
    suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending>
    suspend fun getTopRatedMovies(pageNumber: Int): PagedFetchResponse<TopRatedMedia>
    suspend fun getFirstPageTopRatedMovies(): List<TopRatedMedia>
    suspend fun getMoviesByGenre(genre: MovieGenre, pageNumber: Int): PagedFetchResponse<Movie>
    suspend fun getAllRatedMovies(): List<RatedMedia>
    suspend fun deleteMovieRating(movieId: Int): Boolean

    suspend fun getUpcomingMoviesByGenre(
        genre: MovieGenre,
        pageNumber: Int
    ): PagedFetchResponse<UpComingMovie>

    suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean

    suspend fun getAccountMovieStatesById(
        id: Int,
    ): MediaStates
}
