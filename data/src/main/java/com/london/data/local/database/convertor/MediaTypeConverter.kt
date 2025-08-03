package com.london.data.local.database.convertor

import androidx.room.TypeConverter
import com.london.domain.entity.recent.MediaType

class MediaTypeConverter {
    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String {
        return mediaType.name
    }

    @TypeConverter
    fun toMediaType(mediaType: String): MediaType {
        return MediaType.valueOf(mediaType)
    }
}