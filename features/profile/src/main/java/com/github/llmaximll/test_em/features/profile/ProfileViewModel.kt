package com.github.llmaximll.test_em.features.profile

import androidx.lifecycle.ViewModel
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.models.User
import com.github.llmaximll.test_em.core.common.repositories_abstract.CommonRepository
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
    private val commonRepository: CommonRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<CurrentUserState>(CurrentUserState.Init)
    val currentUser: StateFlow<CurrentUserState> =
        _currentUser.asStateFlow()

    private val _favoriteItemsCount = MutableStateFlow(0)
    val favoriteItemsCount: StateFlow<Int> =
        _favoriteItemsCount.asStateFlow()

    init {
        getCurrentUserLocal()
        getFavoriteItemsCount()
    }

    private fun getCurrentUserLocal() = launchWithHandler(
        onException = {
            _currentUser.value = CurrentUserState.NotFound
        }
    ) {
        _currentUser.value = CurrentUserState.Loading

        val result = userRepository.getCurrentUserLocal()
        val data = result.getOrNull()

        if (result.isSuccess && data != null) {
            _currentUser.value = CurrentUserState.Success(data)
        } else {
            _currentUser.value = CurrentUserState.NotFound
        }
    }

    fun getFavoriteItemsCount() = launchWithHandler(
        onException = {
            _favoriteItemsCount.value = 0
        }
    ) {
        _favoriteItemsCount.value = itemRepository.getFavoriteItemsCountLocal()
    }

    fun clearDatabase() = launchWithHandler {
        commonRepository.clearDatabase()
    }
}

sealed class CurrentUserState {

    data object Init : CurrentUserState()

    data object Loading : CurrentUserState()

    data object NotFound : CurrentUserState()

    data class Success(val user: User) : CurrentUserState()
}