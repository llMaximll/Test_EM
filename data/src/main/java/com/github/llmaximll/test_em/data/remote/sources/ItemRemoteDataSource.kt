package com.github.llmaximll.test_em.data.remote.sources

import com.github.llmaximll.test_em.data.remote.api_services.ItemApiService
import com.github.llmaximll.test_em.data.remote.models.ItemDto.Companion.asModel
import com.github.llmaximll.test_em.data.remote.models.ItemsDto
import javax.inject.Inject

interface ItemRemoteDataSource {

    suspend fun getItems(): Result<ItemsDto>
}

class ItemRemoteDataSourceImpl @Inject constructor(
    private val itemApiService: ItemApiService
): ItemRemoteDataSource {

    override suspend fun getItems(): Result<ItemsDto> {
        val response = itemApiService.getItems()
        val data = response.body()

        return if (response.isSuccessful && data != null) {
            Result.success(data)
        } else {
            Result.failure(Throwable(response.message()))
        }
    }
}