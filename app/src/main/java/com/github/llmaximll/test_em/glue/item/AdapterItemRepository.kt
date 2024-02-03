package com.github.llmaximll.test_em.glue.item

import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.data.repositories.ItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdapterItemRepository @Inject constructor(
    private val itemRepository: ItemRepositoryImpl
) : ItemRepository {

    override suspend fun getItemsRemoteOrCache(sort: Sort, tag: Tag): Result<List<Item>> =
        itemRepository.getItemsRemoteOrCache(sort, tag)

    override suspend fun markItemFavoriteLocal(id: String, isFavorite: Boolean) {
        itemRepository.markItemFavoriteLocal(id, isFavorite)
    }

    override fun getAllFavoriteItemIdsFlowLocal(): Flow<List<String>> =
        itemRepository.getAllFavoriteItemIdsFlowLocal()
}