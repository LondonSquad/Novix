package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.london.data.dto.search.KnownForDto
import com.london.data.dto.search.PersonDto

class SearchActorsResponseConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromPersonList(persons: List<PersonDto>): String {
        return gson.toJson(persons)
    }

    @TypeConverter
    fun toPersonList(personsString: String): List<PersonDto> {
        return gson.fromJson(personsString, Array<PersonDto>::class.java).toList()
    }

    @TypeConverter
    fun fromKnownForList(knownFor: List<KnownForDto>): String {
        return gson.toJson(knownFor)
    }

    @TypeConverter
    fun toKnownForList(knownForString: String): List<KnownForDto> {
        return gson.fromJson(knownForString, Array<KnownForDto>::class.java).toList()
    }
}