package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.datasource.local.dto.SearchTvShowsResponseDtoLocal

class SearchTvShowsResponseConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchTvShowsResponseDtoList(value: List<SearchTvShowsResponseDtoLocal>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchTvShowsResponseDtoList(value: String): List<SearchTvShowsResponseDtoLocal> {
        val type = object : TypeToken<List<SearchTvShowsResponseDtoLocal>>() {}.type
        return gson.fromJson(value, type)
    }
}