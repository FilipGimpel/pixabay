package com.gimpel.pixabay.search.data.local

import androidx.room.Entity

@Entity(primaryKeys = ["hitId", "searchQuery"])
data class SearchQueryWithHitEntity(
    val hitId: Int,
    val searchQuery: String
)
