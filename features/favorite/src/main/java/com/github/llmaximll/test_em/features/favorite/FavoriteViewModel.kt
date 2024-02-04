package com.github.llmaximll.test_em.features.favorite

import android.content.ClipData.Item
import androidx.lifecycle.ViewModel
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
import com.github.llmaximll.test_em.core.common.states.LocalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    val favoriteItems = itemRepository.getAllFavoriteItemsFlowLocal()

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

