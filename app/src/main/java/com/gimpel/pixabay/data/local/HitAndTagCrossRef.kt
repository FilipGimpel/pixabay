package com.gimpel.pixabay.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["hitId", "tagId"])
data class HitAndTagCrossRef(
    val hitId: Int,
    @ColumnInfo(index = true)
    val tagId: String
)