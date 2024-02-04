package com.github.llmaximll.test_em.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.llmaximll.test_em.data.local.models.UserEntity

@Dao
interface UserDao {

    @Query("DELETE FROM user")
    suspend fun deleteAllFromUser(): Int

    @Insert
    suspend fun insertUser(user: UserEntity): Long?

    @Query("""
        SELECT
        EXISTS (SELECT id FROM user)
    """)
    suspend fun isUserLoggedIn(): Boolean

    @Query("""
        SELECT *
        FROM user
        LIMIT 1
    """)
    suspend fun getCurrentUser(): UserEntity?
}