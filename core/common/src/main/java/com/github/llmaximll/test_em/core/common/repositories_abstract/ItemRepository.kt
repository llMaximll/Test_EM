package com.github.llmaximll.test_em.core.common.repositories_abstract

import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.models.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun getItemsRemoteOrCache(sort: Sort, tag: Tag): Result<List<Item>>

    suspend fun markItemFavoriteLocal(id: String, isFavorite: Boolean)

    fun getAllFavoriteItemIdsFlowLocal(): Flow<List<String>>

    fun getAllFavoriteItemsFlowLocal(): Flow<List<Item>>

    suspend fun getItemByIdLocal(id: String): Item?

    suspend fun getFavoriteItemsCountLocal(): Int
}