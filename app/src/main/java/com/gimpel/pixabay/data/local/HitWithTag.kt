package com.gimpel.pixabay.data.local

import androidx.room.Entity

@Entity(primaryKeys = ["hitId", "query"])
data class QueryWithHitEntity(
    val hitId: Int,
    val query: String
)
