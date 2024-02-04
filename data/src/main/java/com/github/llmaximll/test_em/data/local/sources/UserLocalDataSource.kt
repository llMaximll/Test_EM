package com.github.llmaximll.test_em.data.local.sources

import com.github.llmaximll.test_em.data.local.daos.UserDao
import com.github.llmaximll.test_em.data.local.models.UserEntity
import javax.inject.Inject

interface UserLocalDataSource {

    suspend fun deleteAllFromUser(): Int

    suspend fun insertUser(user: UserEntity): Long?

    suspend fun isUserLoggedIn(): Boolean

    suspend fun getCurrentUser(): UserEntity?
}

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserLocalDataSource {

    override suspend fun deleteAllFromUser(): Int =
        userDao.deleteAllFromUser()

    override suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(user)

    override suspend fun isUserLoggedIn(): Boolean =
        userDao.isUserLoggedIn()

    override suspend fun getCurrentUser(): UserEntity? =
        userDao.getCurrentUser()
}