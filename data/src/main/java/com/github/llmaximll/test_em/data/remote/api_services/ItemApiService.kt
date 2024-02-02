package com.github.llmaximll.test_em.data.remote.api_services

import com.github.llmaximll.test_em.data.remote.models.ItemsDto
import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {

    @GET("97e721a7-0a66-4cae-b445-83cc0bcf9010")
    suspend fun getItems(): Response<ItemsDto>
}