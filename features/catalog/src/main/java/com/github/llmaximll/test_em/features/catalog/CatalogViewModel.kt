package com.github.llmaximll.test_em.features.catalog

import androidx.lifecycle.ViewModel
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _itemsFetchState = MutableStateFlow<FetchState<List<Item>>>(FetchState.Init)
    val itemsFetchState: StateFlow<FetchState<List<Item>>> =
        _itemsFetchState.asStateFlow()

    init {
        fetchItemsRemote()
    }

    private fun fetchItemsRemote() = launchWithHandler(
        onException = {
            _itemsFetchState.value = FetchState.Error(it)
        }
    ) {
        _itemsFetchState.value = FetchState.Loading

        val result = itemRepository.getItemsRemote()
        val data = result.getOrNull()

        if (result.isSuccess && data != null) {
            _itemsFetchState.value = FetchState.Success(data)
        } else {
            _itemsFetchState.value = FetchState.Error(NoSuchElementException())
        }
    }
}

sealed class FetchState<out T> {

    data object Init : FetchState<Nothing>()

    data object Loading : FetchState<Nothing>()

    data class Error(val throwable: Throwable? = null) : FetchState<Nothing>()

    data class Success<T>(val data: List<Item>) : FetchState<T>()
}