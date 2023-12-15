package com.omarahmed.dogsample.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DogService {

    companion object {
        private const val API_KEY =
            "live_YJo8baAjOk1U86prlu1IdWZn6t8Xu7x1NQj6zxqEwIHKAVSkrLTEb6uS9LEfUo0E"
        private const val DEFAULT_LIMIT = 50
        private const val DEFAULT_HAS_BREEDS = true
        private const val DEFAULT_IMAGE_SIZE = "small"
    }


    @GET("v1/images/search")
    suspend fun getAll(
        @Header("x-api-key") apiKey: String = API_KEY,
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("has_breeds") hasBreeds: Boolean = DEFAULT_HAS_BREEDS,
        @Query("size") imageSize: String = DEFAULT_IMAGE_SIZE,
    ): Response<List<RemoteDog>>

}