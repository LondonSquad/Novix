package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.datasource.local.utils.fromJsonList


class CommonConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromIntList(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> = gson.fromJsonList<Int>(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = gson.fromJsonList<String>(value)

}
