package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.local.utils.jsonList


class CommonConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromIntList(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> = gson.jsonList<Int>(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = gson.jsonList<String>(value)

}
