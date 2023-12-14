package com.omarahmed.dogsample.data

import com.omarahmed.dogsample.model.Breed
import com.omarahmed.dogsample.model.Dog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogRemoteDataSource @Inject constructor(
    private val dogService: DogService
) {

    suspend fun getAll(): List<Dog> {
        val response = dogService.getAll()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return parse(body)
        } else {
            throw Exception("code = ${response.code()}, body = $body")
        }
    }

    private fun parse(remoteList: List<RemoteDog>): List<Dog> {
        val returnList = mutableListOf<Dog>()
        for (item in remoteList) {
            val id = item.id ?: continue
            val image = item.image.orEmpty()
            val breeds = item.breeds.orEmpty().map { convertBreed(it) }
            returnList += Dog(id, image, breeds)
        }
        return returnList.toList()
    }

    private fun convertBreed(remoteBreed: RemoteBreed): Breed {
        return with(remoteBreed) {
            Breed(
                id.orEmpty(),
                name.orEmpty(),
                group.orEmpty(),
                lifeSpan.orEmpty(),
                temperament.orEmpty()
            )
        }
    }

}