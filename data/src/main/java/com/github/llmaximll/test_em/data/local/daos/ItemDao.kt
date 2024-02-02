package com.github.llmaximll.test_em.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.llmaximll.test_em.data.local.models.ItemEntity

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Query(
        """
        SELECT *
        FROM item
    """
    )
    suspend fun getAllItems(): List<ItemEntity>
}