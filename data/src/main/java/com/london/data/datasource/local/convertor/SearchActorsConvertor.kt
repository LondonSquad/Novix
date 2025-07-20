package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.datasource.local.model.KnownForDtoLocal
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.utils.fromJsonList


class SearchActorsConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromPersonList(persons: List<PersonDtoLocal>): String = gson.toJson(persons)

    @TypeConverter
    fun toPersonList(personsString: String): List<PersonDtoLocal> =
        gson.fromJsonList<PersonDtoLocal>(personsString)

    @TypeConverter
    fun fromKnownForList(knownFor: List<KnownForDtoLocal>): String =
        gson.toJson(knownFor)

    @TypeConverter
    fun toKnownForList(knownForString: String): List<KnownForDtoLocal> =
        gson.fromJsonList<KnownForDtoLocal>(knownForString)
}
