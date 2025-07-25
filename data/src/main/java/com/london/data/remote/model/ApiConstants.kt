@file:KoverIgnore
package com.london.data.remote.model

import com.london.domain.KoverIgnore

object ApiConstants {
    const val SEARCH_PATH_MOVIES = "3/search/movie"
    const val SEARCH_PATH_TVS = "3/search/tv"
    const val SEARCH_PATH_ACTORS = "3/search/person"
    private const val MOVIE_DETAILS_PATH = "3/movie"
    const val MOVIE_DISCOVER_PATH = "3/discover/movie"
    const val POPULAR_MOVIES_PATH = "3/movie/popular"
    const val POPULAR_TV_SHOWS_PATH = "3/tv/popular"
    const val TRENDING_MOVIES_PATH = "3/trending/movie/day"
    const val TRENDING_TV_SHOWS_PATH = "3/trending/tv/day"
    const val TRENDING_ACTORS_PATH = "3/trending/person/day"

    fun getTvShowDetailsPath(tvShowId: Int) = "3/tv/$tvShowId"
    fun getCastTvShowPath(tvShowId: Int) = "3/tv/$tvShowId/aggregate_credits"
    fun getImagesTvShowPath(tvShowId: Int) = "3/tv/$tvShowId/images"
    fun getTvShowEpisodeBySeasonPath(tvShowId: Int, seasonNumber: Int) =
        "3/tv/$tvShowId/season/$seasonNumber"
    fun getTvShowEpisodeByEpisodePath(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = "3/tv/$tvShowId/season/$seasonNumber/episode/$episodeNumber"
    fun getActorDetailsPath(actorId: Int) = "3/person/$actorId"
    fun getActorMoviesPath(actorId: Int) = "3/person/$actorId/movie_credits"
    fun getActorTvShowsPath(actorId: Int) = "3/person/$actorId/tv_credits"
    fun getActorImagePath(actorId: Int) = "3/person/$actorId/images"

    fun getMovieDetailsPath(movieId: Int) = "$MOVIE_DETAILS_PATH/$movieId"
    fun getSimilarMoviesPath(movieId: Int) = "$MOVIE_DETAILS_PATH/$movieId/similar"
    fun getMovieCastPath(movieId: Int) = "$MOVIE_DETAILS_PATH/$movieId/credits"
    fun getMovieImagesPath(movieId: Int) = "$MOVIE_DETAILS_PATH/$movieId/images"

    fun getMovieVideosPath(movieId: Int) = "$MOVIE_DETAILS_PATH/$movieId/videos"

    fun getMovieReviewsPath(movieId: Int) = "3/movie/$movieId/reviews"
    fun getTvShowReviewsPath(tvShowId: Int) = "3/tv/$tvShowId/reviews"

    fun getTvShowVideosPath(tvShowId: Int) = "3/tv/$tvShowId/videos"
}