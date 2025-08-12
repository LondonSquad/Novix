@file:KoverIgnore

package com.london.data.remote.model

import com.london.domain.KoverIgnore

object ApiConstants {
    const val SEARCH_PATH_MOVIES = "3/search/movie"
    const val SEARCH_PATH_TVS = "3/search/tv"
    const val SEARCH_PATH_ACTORS = "3/search/person"
    const val MOVIE_DISCOVER_PATH = "3/discover/movie"
    const val TV_SHOW_DISCOVER_PATH = "3/discover/tv"
    const val POPULAR_MOVIES_PATH = "3/movie/popular"
    const val POPULAR_TV_SHOWS_PATH = "3/tv/popular"
    const val TRENDING_MOVIES_PATH = "3/trending/movie/day"
    const val TRENDING_TV_SHOWS_PATH = "3/trending/tv/day"
    const val TRENDING_ACTORS_PATH = "3/trending/person/day"
    const val MOVIE_DETAILS_PATH = "3/movie/{movie_id}"
    const val SIMILAR_MOVIES_PATH = "3/movie/{movie_id}/similar"
    const val MOVIE_IMAGES_PATH = "3/movie/{movie_id}/images"
    const val MOVIE_VIDEOS_PATH = "3/movie/{movie_id}/videos"
    const val ACCOUNT_MOVIE_STATES = "3/movie/{movie_id}/account_states"
    const val ACTOR_MOVIES_PATH = "3/person/{person_id}/movie_credits"
    const val RATED_MOVIES_PATH = "3/account/{account_id}/rated/movies"
    const val ADD_MOVIE_RATING_PATH = "3/movie/{movie_id}/rating"
    const val DELETE_MOVIE_RATING_PATH = "3/movie/{movie_id}/rating"
    const val MOVIE_REVIEW_PATH = "3/movie/{movie_id}/reviews"
    const val TOP_RATED_MOVIES_PATH = "3/movie/top_rated"
    const val TV_SHOW_DETAILS_PATH = "3/tv/{tv_id}"
    const val TV_SHOW_EPISODE_BY_SEASON_PATH = "3/tv/{tv_id}/season/{season_number}"
    const val TV_SHOW_IMAGE = "3/tv/{tv_id}/images"
    const val EPISODE_DETAILS_PATH = "3/tv/{tv_id}/season/{season_number}/episode/{episode_number}"
    const val TV_SHOW_VIDEO_PATH = "3/tv/{tv_id}/videos"
    const val ACCOUNT_TV_SHOW_STATES = "3/tv/{series_id}/account_states"
    const val ACTOR_TV_SHOWS = "3/person/{person_id}/tv_credits"
    const val RATED_TV_SHOWS_PATH = "3/account/{account_id}/rated/tv"
    const val ADD_TV_SHOW_RATING_PATH = "3/tv/{series_id}/rating"
    const val DELETE_TV_SHOW_RATING_PATH = "/3/tv/{series_id}/rating"
    const val GET_TV_SHOW_REVIEW_PATH = "3/tv/{tv_id}/reviews"
    const val GET_TOP_RATED_TV_SHOWS_PATH = "3/tv/top_rated"
    const val GET_ACTOR_DETAILS_PATH = "3/person/{person_id}"
    const val GET_ACTOR_IMAGES_PATH = "3/person/{person_id}/images"
    const val GET_TV_SHOW_CAST = "3/tv/{tv_id}/aggregate_credits"
    const val GET_MOVIE_CAST = "3/movie/{movie_id}/credits"
    const val ADD_TV_EPISODE_PATH =
        "3/tv/{series_id}/season/{season_number}/episode/{episode_number}/rating"
    const val EPISODE_VIDEO_PATH =
        "3/tv/{series_id}/season/{season_number}/episode/{episode_number}/videos"

    const val ACCOUNT_TV_EPISODE =
        "3/tv/{series_id}/season/{season_number}/episode/{episode_number}/account_states"
}