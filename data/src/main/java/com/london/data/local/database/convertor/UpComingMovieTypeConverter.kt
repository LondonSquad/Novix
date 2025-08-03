package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.local.model.home.upcoming.UpComingMovieDtoLocal

class UpComingMovieTypeConverter {

    @TypeConverter
    fun fromUpComingMovieList(movies: List<UpComingMovieDtoLocal>): String {
        return Gson().toJson(movies)
    }

    @TypeConverter
    fun toUpComingMovieList(moviesString: String): List<UpComingMovieDtoLocal> {
        val listType = object : TypeToken<List<UpComingMovieDtoLocal>>() {}.type
        return Gson().fromJson(moviesString, listType)
    }
}