package com.github.llmaximll.test_em.data.remote.di

import com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSource
import com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataSourcesModule {

    @Binds
    fun bindItemRemoteDataSource(
        impl: ItemRemoteDataSourceImpl
    ): ItemRemoteDataSource
}