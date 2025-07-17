package com.london.data.datasource.local.convertor

import androidx.room.TypeConverter
import com.london.data.datasource.local.model.recent.MediaTypeLocal

class RecentViewedConverter {

    @TypeConverter
    fun fromMediaType(value: MediaTypeLocal): String = value.name

    @TypeConverter
    fun toMediaType(value: String): MediaTypeLocal = MediaTypeLocal.valueOf(value)

}
