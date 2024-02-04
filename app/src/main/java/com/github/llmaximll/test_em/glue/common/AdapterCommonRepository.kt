package com.github.llmaximll.test_em.glue.common

import com.github.llmaximll.test_em.core.common.repositories_abstract.CommonRepository
import com.github.llmaximll.test_em.data.repositories.CommonRepositoryImpl
import javax.inject.Inject

class AdapterCommonRepository @Inject constructor(
    private val commonRepository: CommonRepositoryImpl
) : CommonRepository {

    override suspend fun clearDatabase() {
        commonRepository.clearDatabase()
    }
}