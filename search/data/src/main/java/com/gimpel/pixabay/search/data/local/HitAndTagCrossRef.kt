package com.gimpel.pixabay.search.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["hitId", "tagId"])
data class HitAndTagCrossRef(
    val hitId: Int,
    @ColumnInfo(index = true)
    val tagId: String
)