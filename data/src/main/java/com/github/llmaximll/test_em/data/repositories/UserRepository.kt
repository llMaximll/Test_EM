package com.github.llmaximll.test_em.data.repositories

import com.github.llmaximll.test_em.core.common.models.User
import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import com.github.llmaximll.test_em.data.local.models.asEntity
import com.github.llmaximll.test_em.data.local.sources.UserLocalDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {

    override suspend fun insertUserLocal(user: User): Long? =
        userLocalDataSource.insertUser(user.asEntity())

    override suspend fun isUserLoggedInLocal(): Boolean =
        userLocalDataSource.isUserLoggedIn()
}