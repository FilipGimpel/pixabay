package com.gimpel.pixabay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "hit"
)
@TypeConverters(Converters::class)
data class LocalHit(
    @PrimaryKey val id: Int,
    val previewURL: String,
    val user: String,
    val tags: List<String>,

    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)