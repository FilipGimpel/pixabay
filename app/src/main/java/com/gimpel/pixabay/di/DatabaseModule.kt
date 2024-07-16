package com.gimpel.pixabay.di

import android.content.Context
import androidx.room.Room
import com.gimpel.pixabay.search.data.local.PixabayDao
import com.gimpel.pixabay.search.data.local.PixabayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): PixabayDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PixabayDatabase::class.java,
            "Pixabay.db"
        ).build()
    }

    @Provides
    fun providePixabayDao(database: PixabayDatabase): PixabayDao = database.pixabayDao()
}