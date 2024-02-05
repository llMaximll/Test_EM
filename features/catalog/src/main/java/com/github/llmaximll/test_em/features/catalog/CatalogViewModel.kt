package com.github.llmaximll.test_em.features.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.Tag
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _itemsFetchState = MutableStateFlow<FetchState<List<Item>>>(FetchState.Init)
    val itemsFetchState: StateFlow<FetchState<List<Item>>> =
        _itemsFetchState.asStateFlow()

    val favoriteItemIds = itemRepository.getAllFavoriteItemIdsFlowLocal()

    private val _currentSort = MutableStateFlow(Sort.Standard)
    val currentSort: StateFlow<Sort> = _currentSort.asStateFlow()
        .onEach {
            fetchItemsRemote()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Sort.Standard
        )

    private val _currentTag = MutableStateFlow<Tag?>(Tag.All)
    val currentTag: StateFlow<Tag?> = _currentTag.asStateFlow()
        .onEach {
            fetchItemsRemote()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Tag.All
        )

    init {
        fetchItemsRemote()
    }

    private var fetchItemsJob: Job? = null
    fun fetchItemsRemote() {
        fetchItemsJob?.cancel()
        fetchItemsJob = launchWithHandler(
            onException = {
                _itemsFetchState.value = FetchState.Error(it)
            }
        ) {
            /*_itemsFetchState.value = FetchState.Loading

            delay(1500L)

            _itemsFetchState.value = FetchState.Error(IllegalStateException())*/

            if (itemsFetchState.firstOrNull() is FetchState.Loading)
                return@launchWithHandler

            _itemsFetchState.value = FetchState.Loading

            log("fetchItemsRemote:: sort: ${currentSort.value} tag: ${currentTag.value}")

            val result = itemRepository.getItemsRemoteOrCache(
                sort = currentSort.value,
                tag = currentTag.value ?: Tag.All
            )
            val data = result.getOrNull()

            if (result.isSuccess && data != null) {
                _itemsFetchState.value = FetchState.Success(data)
            } else {
                _itemsFetchState.value = FetchState.Error(NoSuchElementException())
            }
        }
    }

    private var markItemJob: Job? = null
    fun markItemFavorite(
        id: String,
        isFavorite: Boolean
    ) {
        markItemJob?.cancel()
        markItemJob = launchWithHandler {
            itemRepository.markItemFavoriteLocal(id, isFavorite)
        }
    }

    fun changeSort(newSort: Sort) {
        _currentSort.value = newSort
    }

    fun changeTag(newTag: Tag?) {
        _currentTag.value = newTag
    }
}

sealed class FetchState<out T> {

    data object Init : FetchState<Nothing>()

    data object Loading : FetchState<Nothing>()

    data class Error(val throwable: Throwable? = null) : FetchState<Nothing>()

    data class Success<T>(val data: List<Item>) : FetchState<T>()
}