package com.github.llmaximll.test_em.ui.main

import androidx.lifecycle.ViewModel
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    val isUserLoggedIn: StateFlow<Boolean?> = _isUserLoggedIn.asStateFlow()

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() = launchWithHandler(
        onException = {
            _isUserLoggedIn.value = false
        }
    ) {
        _isUserLoggedIn.value = null

        _isUserLoggedIn.value = userRepository.isUserLoggedIn()
    }
}