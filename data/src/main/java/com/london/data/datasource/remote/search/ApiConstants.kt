package com.london.data.datasource.remote.search

object ApiConstants {
    const val SEARCH_HOST = "api.themoviedb.org"
    const val SEARCH_PATH_MOVIES = "3/search/movie"
    const val SEARCH_PATH_TVS = "3/search/tv"
    const val SEARCH_PATH_ACTORS = "3/search/person"
    const val ACTOR_DETAILS = "3/person/{person_id}"
    const val ACTOR_MOVIES_CREDITS = "3/person/{person_id}/movie_credits"
    const val ACTOR_TV_SHOWS_CREDITS = "3/person/{person_id}/tv_credits"
}
