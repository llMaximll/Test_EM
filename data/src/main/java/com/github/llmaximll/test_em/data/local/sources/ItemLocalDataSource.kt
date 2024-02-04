package com.github.llmaximll.test_em.data.local.sources

import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.data.local.daos.ItemDao
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ItemLocalDataSource {

    suspend fun deleteAllFromItem(): Int

    suspend fun insertItems(items: List<ItemEntity>)

    suspend fun getAllItems(sort: Sort, tag: Tag): List<ItemEntity>

    suspend fun markItemFavorite(id: String, isFavorite: Boolean)

    fun getAllFavoriteItemIdsFlow(): Flow<List<String>>

    suspend fun getItemById(id: String): ItemEntity?
}

class ItemLocalDataSourceImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemLocalDataSource {

    override suspend fun deleteAllFromItem(): Int =
        itemDao.deleteAllFromItem()

    override suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insertItems(items)
    }

    override suspend fun getAllItems(sort: Sort, tag: Tag): List<ItemEntity> = when (sort) {
        Sort.Standard -> itemDao.getAllItems(tag.tag)
        Sort.Popular -> itemDao.getAllItemsOrderByPopular(tag.tag)
        Sort.ReducingPrice -> itemDao.getAllItemsOrderByPriceDesc(tag.tag)
        Sort.AscendingPrice -> itemDao.getAllItemsOrderByPrice(tag.tag)
    }

    override suspend fun markItemFavorite(id: String, isFavorite: Boolean) {
        itemDao.markItemFavorite(id, isFavorite)
    }

    override fun getAllFavoriteItemIdsFlow(): Flow<List<String>> =
        itemDao.getAllFavoriteItemIdsFlow()

    override suspend fun getItemById(id: String): ItemEntity? =
        itemDao.getItemById(id)
}