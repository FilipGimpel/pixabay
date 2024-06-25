package com.gimpel.pixabay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "hit"
)
data class LocalHit(
    @PrimaryKey val id: Int,
    val previewURL: String,
    val user: String,
    val tags: String,

    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)