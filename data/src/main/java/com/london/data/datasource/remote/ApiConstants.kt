package com.london.data.datasource.remote

object ApiConstants {
    const val HOST = "api.themoviedb.org"
    const val SEARCH_PATH_MOVIES = "3/search/movie"
    const val SEARCH_PATH_TVS = "3/search/tv"
    const val SEARCH_PATH_ACTORS = "3/search/person"

    fun getTvShowDetailsPath(tvShowId: Int) = "3/tv/$tvShowId"
    fun getCastTvShowPath(tvShowId: Int) = "3/tv/$tvShowId/credits"
    fun getImagesTvShowPath(tvShowId: Int) = "3/tv/$tvShowId/images"

    fun getActorDetailsPath(actorId: Int) = "3/person/$actorId"
    fun getActorMoviesPath(actorId: Int) = "3/person/$actorId/movie_credits"
    fun getActorTvShowsPath(actorId: Int) = "3/person/$actorId/tv_credits"
    fun getActorImagePath(actorId: Int) = "3/person/$actorId/images"
}
