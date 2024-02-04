package com.github.llmaximll.test_em.glue.common.di

import com.github.llmaximll.test_em.core.common.repositories_abstract.CommonRepository
import com.github.llmaximll.test_em.glue.common.AdapterCommonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdapterCommonModule {

    @Binds
    fun bindAdapterCommonRepository(
        adapter: AdapterCommonRepository
    ): CommonRepository
}