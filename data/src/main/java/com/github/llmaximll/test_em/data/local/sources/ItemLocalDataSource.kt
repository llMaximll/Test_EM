package com.github.llmaximll.test_em.data.local.sources

import com.github.llmaximll.test_em.data.local.daos.ItemDao
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import javax.inject.Inject

interface ItemLocalDataSource {

    suspend fun insertItems(items: List<ItemEntity>)

    suspend fun getAllItems(): List<ItemEntity>
}

class ItemLocalDataSourceImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemLocalDataSource {

    override suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insertItems(items)
    }

    override suspend fun getAllItems(): List<ItemEntity> =
        itemDao.getAllItems()
}