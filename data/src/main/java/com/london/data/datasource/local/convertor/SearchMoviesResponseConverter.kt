package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.london.data.datasource.local.dto.SearchMoviesResponse
import com.london.data.datasource.local.dto.SearchMoviesResponseDto

class SearchMoviesResponseConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchMoviesResponse(value: SearchMoviesResponse): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSearchMoviesResponse(json: String): SearchMoviesResponse {
        return gson.fromJson(json, SearchMoviesResponse::class.java)
    }

    @TypeConverter
    fun fromSearchMoviesResponseDtoList(list: List<SearchMoviesResponseDto>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toSearchMoviesResponseDtoList(json: String): List<SearchMoviesResponseDto> {
        val type = object : TypeToken<List<SearchMoviesResponseDto>>() {}.type
        return gson.fromJson(json, type)
    }
}