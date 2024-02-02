package com.github.llmaximll.test_em.data.local.di

import com.github.llmaximll.test_em.data.local.sources.UserLocalDataSource
import com.github.llmaximll.test_em.data.local.sources.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {

    @Binds
    fun bindUserLocalDataSource(
        impl: UserLocalDataSourceImpl
    ): UserLocalDataSource
}