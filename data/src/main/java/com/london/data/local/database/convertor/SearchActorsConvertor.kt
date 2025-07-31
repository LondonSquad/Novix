package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.local.model.search.ActorLocal
import com.london.data.local.utils.fromJsonList


class SearchActorsConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromPersonList(persons: List<ActorLocal>): String = gson.toJson(persons)

    @TypeConverter
    fun toPersonList(personsString: String): List<ActorLocal> =
        gson.fromJsonList<ActorLocal>(personsString)
}
