package com.kotlin.campusconnect.db

import androidx.room.TypeConverter
import com.kotlin.campusconnect.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name ?: ""
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}