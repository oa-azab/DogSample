package com.omarahmed.dogsample.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface DogService {

    @Headers("x-api-key: live_YJo8baAjOk1U86prlu1IdWZn6t8Xu7x1NQj6zxqEwIHKAVSkrLTEb6uS9LEfUo0E")
    @GET("v1/images/search?limit=50&has_breeds=1")
    suspend fun getAll(): Response<List<RemoteDog>>

}