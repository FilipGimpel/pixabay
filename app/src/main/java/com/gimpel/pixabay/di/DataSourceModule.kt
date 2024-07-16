package com.gimpel.pixabay.di

import com.gimpel.pixabay.search.data.local.LocalHitDataSource
import com.gimpel.pixabay.search.data.local.LocalHitDataSourceImpl
import com.gimpel.pixabay.search.data.network.RemoteHitDataSource
import com.gimpel.pixabay.search.data.network.RemoteHitDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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