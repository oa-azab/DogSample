package com.omarahmed.dogsample.domain

import com.omarahmed.dogsample.UCResult
import com.omarahmed.dogsample.data.DogRepository
import com.omarahmed.dogsample.model.Dog
import javax.inject.Inject

class GetAllDogsUseCase @Inject constructor(
    private val dogRepository: DogRepository
) {

    suspend fun invoke(
        forceRefresh: Boolean = false,
        filterTerm: String = ""
    ): UCResult<List<Dog>> {
        return try {
            val dogs = dogRepository.getAll(forceRefresh)
            if (filterTerm.isNotBlank()) {
                val filtered = dogs.filter {
                    it.breed.name.contains(
                        filterTerm,
                        ignoreCase = true
                    )
                }
                UCResult.Success(filtered)
            } else {
                UCResult.Success(dogs)
            }
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }


}