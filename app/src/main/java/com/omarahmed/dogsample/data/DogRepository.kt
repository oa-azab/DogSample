package com.omarahmed.dogsample.data

import com.omarahmed.dogsample.model.Dog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogRepository @Inject constructor(
    private val remoteDataSource: DogRemoteDataSource
) {

    private var cache: List<Dog>? = null

    suspend fun getAll(forceRefresh: Boolean = false): List<Dog> {
        if (forceRefresh) cache = null

        val cacheCopy = cache
        if (cacheCopy != null) return cacheCopy

        val dogs = remoteDataSource.getAll()
        cache = dogs

        return dogs
    }
}