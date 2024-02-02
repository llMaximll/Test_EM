package com.github.llmaximll.test_em.glue.item.di

import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.glue.item.AdapterItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdapterItemModule {

    @Binds
    fun bindAdapterItemRepository(
        adapter: AdapterItemRepository
    ): ItemRepository
}