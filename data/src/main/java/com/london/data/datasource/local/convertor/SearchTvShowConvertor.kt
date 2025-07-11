package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.datasource.local.model.SearchTvShowDtoLocal

class SearchTvShowConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchTvShowsResponseDtoList(value: List<SearchTvShowDtoLocal>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchTvShowsResponseDtoList(value: String): List<SearchTvShowDtoLocal> {
        val type = object : TypeToken<List<SearchTvShowDtoLocal>>() {}.type
        return gson.fromJson(value, type)
    }
}