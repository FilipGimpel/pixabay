package com.gimpel.pixabay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HitEntity::class, SearchQueryWithHitEntity::class], version = 1, exportSchema = false)
abstract class PixabayDatabase  : RoomDatabase() {
    abstract fun pixabayDao(): PixabayDao
}