package com.github.llmaximll.test_em.features.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.test_em.core.common.Sort
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.core.common.states.LocalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = savedStateHandle["productId"] ?: "-1"

    private val _itemState = MutableStateFlow<LocalUiState<Item>>(LocalUiState.Init)
    val itemState: StateFlow<LocalUiState<Item>> = _itemState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavorite: Flow<Boolean> = itemRepository.getAllFavoriteItemIdsFlowLocal().mapLatest { list ->
        list.any { it == productId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    init {
        getItemById()
    }

    private fun getItemById() = launchWithHandler(
        onException = {
            _itemState.value = LocalUiState.NotFound
        }
    ) {
        _itemState.value = LocalUiState.Loading

        val data = itemRepository.getItemByIdLocal(productId)

        if (data != null) {
            _itemState.value = LocalUiState.Success(data)
        } else {
            _itemState.value = LocalUiState.NotFound
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
}