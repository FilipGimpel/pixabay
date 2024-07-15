package com.gimpel.pixabay.search.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey
    val tagId: String
)