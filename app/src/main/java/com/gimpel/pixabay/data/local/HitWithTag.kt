package com.gimpel.pixabay.data.local

import androidx.room.Entity

@Entity(primaryKeys = ["hitId", "searchQuery"])
data class SearchQueryWithHitEntity(
    val hitId: Int,
    val searchQuery: String
)
