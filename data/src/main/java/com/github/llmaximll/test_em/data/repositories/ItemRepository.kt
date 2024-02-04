package com.github.llmaximll.test_em.data.repositories

import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.data.local.models.ItemEntity.Companion.asModel
import com.github.llmaximll.test_em.data.local.sources.ItemLocalDataSource
import com.github.llmaximll.test_em.data.remote.models.ItemDto.Companion.asEntity
import com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemRemoteDataSource: ItemRemoteDataSource,
    private val itemLocalDataSource: ItemLocalDataSource
) : ItemRepository {

    override suspend fun getItemsRemoteOrCache(sort: Sort, tag: Tag): Result<List<Item>> {
        val localResult = itemLocalDataSource.getAllItems(sort, tag)

        return if (localResult.isNotEmpty()) {
            log("getItemsRemote:: Local result: $localResult")
            Result.success(localResult.map { it.asModel() })
        } else {
            val remoteResult = itemRemoteDataSource.getItems()
            val data = remoteResult.getOrNull()?.items

            log("getItemsRemote:: Remote result: $data")

            if (remoteResult.isSuccess && !data.isNullOrEmpty()) {
                itemLocalDataSource.insertItems(data.mapNotNull { it.asEntity() })

                val newLocalResult = itemLocalDataSource.getAllItems(sort, tag)
                Result.success(newLocalResult.map { it.asModel() })
            } else {
                Result.failure(NoSuchElementException())
            }
        }
    }

    override suspend fun markItemFavoriteLocal(id: String, isFavorite: Boolean) {
        itemLocalDataSource.markItemFavorite(id, isFavorite)
    }

    override fun getAllFavoriteItemIdsFlowLocal(): Flow<List<String>> =
        itemLocalDataSource.getAllFavoriteItemIdsFlow()

    override fun getAllFavoriteItemsFlowLocal(): Flow<List<Item>> =
        itemLocalDataSource.getAllFavoriteItemsFlow().map { list -> list.map { it.asModel() } }

    override suspend fun getItemByIdLocal(id: String): Item? =
        itemLocalDataSource.getItemById(id)?.asModel()

    override suspend fun getFavoriteItemsCountLocal(): Int =
        itemLocalDataSource.getAllFavoriteItemIdsFlow().firstOrNull()?.size ?: 0
}