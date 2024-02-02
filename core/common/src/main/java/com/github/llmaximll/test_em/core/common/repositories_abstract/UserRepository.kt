package com.github.llmaximll.test_em.core.common.repositories_abstract

import com.github.llmaximll.test_em.core.common.models.User

interface UserRepository {
    
    suspend fun insertUser(user: User): Long?

    suspend fun isUserLoggedIn(): Boolean
}