package com.github.llmaximll.test_em.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ViewModel.launchWithHandler(
    onException: (Throwable) -> Unit = { },
    block: suspend () -> Unit
) =
    viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            throwable.message?.let { err(it) }
            onException(throwable)
        }
    ) {
        block()
    }