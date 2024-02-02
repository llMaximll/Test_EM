package com.github.llmaximll.test_em.glue.item

import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.data.repositories.ItemRepositoryImpl
import javax.inject.Inject

class AdapterItemRepository @Inject constructor(
    private val itemRepository: ItemRepositoryImpl
) : ItemRepository {

    override suspend fun getItemsRemote(): Result<List<Item>> =
        itemRepository.getItemsRemote()
}