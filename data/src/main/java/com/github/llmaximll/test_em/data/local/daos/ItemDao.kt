package com.github.llmaximll.test_em.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Query(
        """
        SELECT *
        FROM item
        WHERE :tag IS NULL OR instr(tags, :tag) > 0
    """
    )
    suspend fun getAllItems(tag: String?): List<ItemEntity>

    @Query(
        """
        SELECT *
        FROM item
        WHERE :tag IS NULL OR instr(tags, :tag) > 0
        ORDER BY rating DESC
    """
    )
    suspend fun getAllItemsOrderByPopular(tag: String?): List<ItemEntity>

    @Query(
        """
        SELECT *
        FROM item
        WHERE :tag IS NULL OR instr(tags, :tag) > 0
        ORDER BY priceWithDiscount DESC
    """
    )
    suspend fun getAllItemsOrderByPriceDesc(tag: String?): List<ItemEntity>

    @Query(
        """
        SELECT *
        FROM item
        WHERE :tag IS NULL OR instr(tags, :tag) > 0
        ORDER BY priceWithDiscount
    """
    )
    suspend fun getAllItemsOrderByPrice(tag: String?): List<ItemEntity>

    @Query(
        """
        UPDATE item
        SET isFavorite = :isFavorite
        WHERE id = :id
    """
    )
    suspend fun markItemFavorite(id: String, isFavorite: Boolean)

    @Query(
        """
        SELECT id
        FROM item
        WHERE isFavorite = 1
    """
    )
    fun getAllFavoriteItemIdsFlow(): Flow<List<String>>

    @Query(
        """
        SELECT *
        FROM item
        WHERE id = :id
    """
    )
    suspend fun getItemById(id: String): ItemEntity?
}