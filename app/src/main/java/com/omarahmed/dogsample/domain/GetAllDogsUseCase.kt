package com.omarahmed.dogsample.domain

import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.data.DogRepository
import com.omarahmed.dogsample.model.Dog
import javax.inject.Inject

class GetAllDogsUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {

    suspend fun invoke(): UCResult<List<Dog>> {
        return try {
            val dogs = dogRepository.getAll()
            UCResult.Success(dogs)
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }


}