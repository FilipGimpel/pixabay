package com.gimpel.pixabay.data.local

import androidx.room.TypeConverter

/**
 * Converts a list of strings to a single string and vice versa.
 * this should be done using a many-to-many relationship in a real app.
 */
class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}