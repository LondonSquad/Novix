package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.dto.search.SearchTvShowsResponseDto

class SearchTvShowsResponseConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchTvShowsResponseDtoList(value: List<SearchTvShowsResponseDto>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchTvShowsResponseDtoList(value: String): List<SearchTvShowsResponseDto> {
        val type = object : TypeToken<List<SearchTvShowsResponseDto>>() {}.type
        return gson.fromJson(value, type)
    }
}