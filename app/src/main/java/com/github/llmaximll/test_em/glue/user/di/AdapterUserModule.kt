package com.github.llmaximll.test_em.glue.user.di

import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import com.github.llmaximll.test_em.glue.user.AdapterUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AdapterUserModule {

    @Binds
    fun bindAdapterUserRepository(
        adapter: AdapterUserRepository
    ): UserRepository
}