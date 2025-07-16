package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.london.data.datasource.local.model.recent.MediaType

class RecentViewedConverter {

    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name

    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)

}
