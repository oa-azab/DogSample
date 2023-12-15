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
            return parseResponse(body)
        } else {
            throw Exception("code = ${response.code()}, body = $body")
        }
    }

    private fun parseResponse(remoteList: List<RemoteDog>): List<Dog> {
        val returnList = mutableListOf<Dog>()
        for (item in remoteList) {
            val id = item.id ?: continue
            val image = item.image.orEmpty()
            val breed = convertBreed(item.breeds?.firstOrNull())
            returnList += Dog(id, image, breed)
        }
        return returnList.toList()
    }

    private fun convertBreed(remoteBreed: RemoteBreed?): Breed {
        return Breed(
            remoteBreed?.id.orEmpty(),
            remoteBreed?.name.orEmpty(),
            remoteBreed?.group.orEmpty(),
            remoteBreed?.lifeSpan.orEmpty(),
            remoteBreed?.temperament.orEmpty()
        )
    }

}