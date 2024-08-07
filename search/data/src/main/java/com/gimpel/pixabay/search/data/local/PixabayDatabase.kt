package com.gimpel.pixabay.search.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        HitEntity::class,
        SearchQueryWithHitEntity::class,
        TagEntity::class,
        HitAndTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PixabayDatabase  : RoomDatabase() {
    abstract fun pixabayDao(): PixabayDao
}