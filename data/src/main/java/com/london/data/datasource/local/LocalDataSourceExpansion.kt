package com.london.data.datasource.local

import com.london.data.datasource.local.LocalDataSourceMessages.DELETE_ACTOR
import com.london.data.datasource.local.LocalDataSourceMessages.DELETE_MOVIE
import com.london.data.datasource.local.LocalDataSourceMessages.DELETE_TV_SHOW
import com.london.data.datasource.local.LocalDataSourceMessages.GET_ACTORS
import com.london.data.datasource.local.LocalDataSourceMessages.GET_ACTOR_BY_DATE
import com.london.data.datasource.local.LocalDataSourceMessages.GET_MOVIES
import com.london.data.datasource.local.LocalDataSourceMessages.GET_MOVIE_BY_DATE
import com.london.data.datasource.local.LocalDataSourceMessages.GET_TV_SHOWS
import com.london.data.datasource.local.LocalDataSourceMessages.GET_TV_SHOW_BY_DATE
import com.london.data.datasource.local.LocalDataSourceMessages.INSERT_ACTOR
import com.london.data.datasource.local.LocalDataSourceMessages.INSERT_MOVIE
import com.london.data.datasource.local.LocalDataSourceMessages.INSERT_TV_SHOW
import com.london.data.datasource.local.LocalDataSourceMessages.UPDATE_ACTOR
import com.london.data.datasource.local.LocalDataSourceMessages.UPDATE_MOVIE
import com.london.data.datasource.local.LocalDataSourceMessages.UPDATE_TV_SHOW

open class LocalDataSourceImplExpansion(message: String) : Exception(message)

open class InsertExpansion(message: String) : LocalDataSourceImplExpansion(message)
class InsertMovieExpansion(message: String = INSERT_MOVIE) : InsertExpansion(message)
class InsertTvShowExpansion(message: String = INSERT_TV_SHOW) : InsertExpansion(message)
class InsertActorExpansion(message: String = INSERT_ACTOR) : InsertExpansion(message)

open class UpdateExpansion(message: String) : LocalDataSourceImplExpansion(message)
class UpdateMovieExpansion(message: String = UPDATE_MOVIE) : UpdateExpansion(message)
class UpdateTvShowExpansion(message: String = UPDATE_TV_SHOW) : UpdateExpansion(message)
class UpdateActorExpansion(message: String = UPDATE_ACTOR) : UpdateExpansion(message)

open class DeleteExpansion(message: String) : LocalDataSourceImplExpansion(message)
class DeleteTvShowExpansion(message: String = DELETE_TV_SHOW) : DeleteExpansion(message)
class DeleteMovieExpansion(message: String = DELETE_MOVIE) : DeleteExpansion(message)
class DeleteActorExpansion(message: String = DELETE_ACTOR) : DeleteExpansion(message)

open class GetAllExpansion(message: String) : LocalDataSourceImplExpansion(message)
class GetMoviesAllExpansion(message: String = GET_MOVIES) : GetAllExpansion(message)
class GetTvShowsAllExpansion(message: String = GET_TV_SHOWS) : GetAllExpansion(message)
class GetActorsAllExpansion(message: String = GET_ACTORS) : GetAllExpansion(message)

open class GetByDateExpansion(message: String) : LocalDataSourceImplExpansion(message)
class GetMovieByDateExpansion(message: String = GET_MOVIE_BY_DATE) : GetByDateExpansion(message)
class GetTvShowByDateExpansion(message: String = GET_TV_SHOW_BY_DATE) : GetByDateExpansion(message)
class GetActorByDateExpansion(message: String = GET_ACTOR_BY_DATE) : GetByDateExpansion(message)

object LocalDataSourceMessages {
    const val INSERT_MOVIE = "Failed to insert movie into the local database."
    const val UPDATE_MOVIE = "Failed to update movie in the local database."
    const val DELETE_MOVIE = "Failed to delete movie from the local database."
    const val GET_MOVIES = "Failed to retrieve movies from the local database."
    const val GET_MOVIE_BY_DATE = "Failed to retrieve movie by date from the local database."

    const val INSERT_TV_SHOW = "Failed to insert TV show into the local database."
    const val UPDATE_TV_SHOW = "Failed to update TV show in the local database."
    const val DELETE_TV_SHOW = "Failed to delete TV show from the local database."
    const val GET_TV_SHOWS = "Failed to retrieve TV shows from the local database."
    const val GET_TV_SHOW_BY_DATE = "Failed to retrieve TV show by date from the local database."

    const val INSERT_ACTOR = "Failed to insert actor into the local database."
    const val UPDATE_ACTOR = "Failed to update actor in the local database."
    const val DELETE_ACTOR = "Failed to delete actor from the local database."
    const val GET_ACTORS = "Failed to retrieve actors from the local database."
    const val GET_ACTOR_BY_DATE = "Failed to retrieve actor by date from the local database."
}