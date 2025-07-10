package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.datasource.local.dto.KnownForDtoLocal
import com.london.data.datasource.local.dto.PersonDtoLocal

class SearchActorsResponseConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromPersonList(persons: List<PersonDtoLocal>): String {
        return gson.toJson(persons)
    }

    @TypeConverter
    fun toPersonList(personsString: String): List<PersonDtoLocal> {
        return gson.fromJson(personsString, Array<PersonDtoLocal>::class.java).toList()
    }

    @TypeConverter
    fun fromKnownForList(knownFor: List<KnownForDtoLocal>): String {
        return gson.toJson(knownFor)
    }

    @TypeConverter
    fun toKnownForList(knownForString: String): List<KnownForDtoLocal> {
        return gson.fromJson(knownForString, Array<KnownForDtoLocal>::class.java).toList()
    }
}