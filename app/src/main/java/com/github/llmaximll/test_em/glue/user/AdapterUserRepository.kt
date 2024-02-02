package com.github.llmaximll.test_em.glue.user

import com.github.llmaximll.test_em.core.common.models.User
import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import com.github.llmaximll.test_em.data.repositories.UserRepositoryImpl
import javax.inject.Inject

class AdapterUserRepository @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : UserRepository {

    override suspend fun insertUserLocal(user: User) =
        userRepository.insertUserLocal(user)

    override suspend fun isUserLoggedInLocal(): Boolean =
        userRepository.isUserLoggedInLocal()
}