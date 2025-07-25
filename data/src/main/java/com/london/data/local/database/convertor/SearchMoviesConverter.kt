package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.local.model.search.SearchMovieDtoLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.local.utils.fromJsonList
import com.london.data.local.utils.fromJsonObject

class SearchMoviesConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchMoviesResponse(value: SearchMoviesLocal): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchMoviesResponse(json: String): SearchMoviesLocal = gson.fromJsonObject(json)


    @TypeConverter
    fun fromSearchMoviesResponseDtoList(list: List<SearchMovieDtoLocal>): String = gson.toJson(list)


    @TypeConverter
    fun toSearchMoviesResponseDtoList(json: String): List<SearchMovieDtoLocal> =
        gson.fromJsonList(json)

}