package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal

class SearchMoviesConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchMoviesResponse(value: SearchMoviesLocal): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchMoviesResponse(json: String): SearchMoviesLocal {
        return gson.fromJson(json, SearchMoviesLocal::class.java)
    }

    @TypeConverter
    fun fromSearchMoviesResponseDtoList(list: List<SearchMovieDtoLocal>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toSearchMoviesResponseDtoList(json: String): List<SearchMovieDtoLocal> {
        val type = object : TypeToken<List<SearchMovieDtoLocal>>() {}.type
        return gson.fromJson(json, type)
    }
}