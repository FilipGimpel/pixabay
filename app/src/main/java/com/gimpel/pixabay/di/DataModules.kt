package com.gimpel.pixabay.di

import android.content.Context
import androidx.room.Room
import com.gimpel.pixabay.search.data.DefaultImagesRepository
import com.gimpel.pixabay.search.data.local.LocalHitDataSource
import com.gimpel.pixabay.search.data.local.LocalHitDataSourceImpl
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import com.gimpel.pixabay.search.data.local.PixabayDao
import com.gimpel.pixabay.search.data.local.PixabayDatabase
import com.gimpel.pixabay.search.data.network.DefaultPixabayService
import com.gimpel.pixabay.search.data.network.PixabayService
import com.gimpel.pixabay.search.data.network.RemoteHitDataSource
import com.gimpel.pixabay.search.data.network.RemoteHitDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Singleton
    @Binds
    abstract fun bindPixabayService(repository: DefaultPixabayService): PixabayService

    @Singleton
    @Binds
    abstract fun bindImagesRepository(repository: DefaultImagesRepository): ImagesRepository
}

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

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindLocalHitDataSource(localHitDataSource: LocalHitDataSourceImpl): LocalHitDataSource

    @Singleton
    @Binds
    abstract fun bindRemoteHitDataSource(remoteHitDataSource: RemoteHitDataSourceImpl): RemoteHitDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object JsonModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())
}