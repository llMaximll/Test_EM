package com.github.llmaximll.test_em.core.common.repositories_abstract

import com.github.llmaximll.test_em.core.common.models.Item

interface ItemRepository {

    suspend fun getItemsRemote(): Result<List<Item>>
}