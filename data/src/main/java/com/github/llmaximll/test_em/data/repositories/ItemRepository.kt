package com.github.llmaximll.test_em.data.repositories

import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.data.local.models.ItemEntity.Companion.asModel
import com.github.llmaximll.test_em.data.local.sources.ItemLocalDataSource
import com.github.llmaximll.test_em.data.remote.models.ItemDto.Companion.asEntity
import com.github.llmaximll.test_em.data.remote.models.ItemDto.Companion.asModel
import com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSource
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemRemoteDataSource: ItemRemoteDataSource,
    private val itemLocalDataSource: ItemLocalDataSource
) : ItemRepository {

    override suspend fun getItemsRemote(): Result<List<Item>> {
        val localResult = itemLocalDataSource.getAllItems()

        return if (localResult.isNotEmpty()) {
            log("getItemsRemote:: Local result: $localResult")
            Result.success(localResult.map { it.asModel() })
        } else {
            val remoteResult = itemRemoteDataSource.getItems()
            val data = remoteResult.getOrNull()?.items

            log("getItemsRemote:: Remote result: $data")

            // Cache in memory
            if (remoteResult.isSuccess && !data.isNullOrEmpty()) {
                itemLocalDataSource.insertItems(data.mapNotNull { it.asEntity() })
            }

            remoteResult.map { resultItems -> resultItems.items.mapNotNull { it.asModel() } }
        }
    }
}