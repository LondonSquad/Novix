package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.datasource.local.model.SearchTvShowDtoLocal
import com.london.data.datasource.local.utils.fromJsonList

class SearchTvShowConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromSearchTvShowsResponseDtoList(value: List<SearchTvShowDtoLocal>): String =
        gson.toJson(value)


    @TypeConverter
    fun toSearchTvShowsResponseDtoList(value: String): List<SearchTvShowDtoLocal> =
        gson.fromJsonList(value)

}