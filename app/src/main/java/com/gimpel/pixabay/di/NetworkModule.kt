package com.gimpel.pixabay.di

import com.gimpel.pixabay.search.data.DefaultImagesRepository
import com.gimpel.pixabay.search.data.network.DefaultPixabayService
import com.gimpel.pixabay.search.data.network.PixabayService
import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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