package com.gimpel.pixabay.search.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class HitWithTags(
    @Embedded
    val hit: HitEntity,

    @Relation(
        parentColumn = "hitId",
        entityColumn = "tagId",
        associateBy = Junction(HitAndTagCrossRef::class)
    )
    val tags: List<TagEntity>
)
