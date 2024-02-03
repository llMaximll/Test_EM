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
}