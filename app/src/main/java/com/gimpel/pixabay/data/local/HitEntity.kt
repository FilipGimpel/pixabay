package com.gimpel.pixabay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HitEntity(
    @PrimaryKey val hitId: Int,
    val previewURL: String,
    val user: String,
    val tags: String,
//    val tags: List<String>,

    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)