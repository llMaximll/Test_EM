package com.github.llmaximll.test_em.data.repositories

import com.github.llmaximll.test_em.core.common.repositories_abstract.CommonRepository
import com.github.llmaximll.test_em.data.local.sources.ItemLocalDataSource
import com.github.llmaximll.test_em.data.local.sources.UserLocalDataSource
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : CommonRepository {

    override suspend fun clearDatabase() {
        itemLocalDataSource.deleteAllFromItem()
        userLocalDataSource.deleteAllFromUser()
    }
}