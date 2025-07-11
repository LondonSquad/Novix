package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson


class CommonConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return gson.fromJson(value, Array<Int>::class.java).toList()
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return gson.fromJson(value, Array<String>::class.java).toList()
    }
}