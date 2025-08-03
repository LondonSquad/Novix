package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.local.model.home.upcoming.UpComingMovieLocal

class UpComingMovieTypeConverter {

    @TypeConverter
    fun fromUpComingMovieList(movies: List<UpComingMovieLocal>): String {
        return Gson().toJson(movies)
    }

    @TypeConverter
    fun toUpComingMovieList(moviesString: String): List<UpComingMovieLocal> {
        val listType = object : TypeToken<List<UpComingMovieLocal>>() {}.type
        return Gson().fromJson(moviesString, listType)
    }
}