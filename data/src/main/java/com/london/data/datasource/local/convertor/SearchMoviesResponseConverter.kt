package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchMoviesResponseDtoLocal

class SearchMoviesResponseConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchMoviesResponse(value: SearchMoviesResponseLocal): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchMoviesResponse(json: String): SearchMoviesResponseLocal {
        return gson.fromJson(json, SearchMoviesResponseLocal::class.java)
    }

    @TypeConverter
    fun fromSearchMoviesResponseDtoList(list: List<SearchMoviesResponseDtoLocal>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toSearchMoviesResponseDtoList(json: String): List<SearchMoviesResponseDtoLocal> {
        val type = object : TypeToken<List<SearchMoviesResponseDtoLocal>>() {}.type
        return gson.fromJson(json, type)
    }
}