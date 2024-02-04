package com.github.llmaximll.test_em.core.common.states

sealed class LocalUiState<out T> {

    data object Init : LocalUiState<Nothing>()

    data object Loading : LocalUiState<Nothing>()

    data object NotFound : LocalUiState<Nothing>()

    data class Success<T>(val data: T) : LocalUiState<T>()
}