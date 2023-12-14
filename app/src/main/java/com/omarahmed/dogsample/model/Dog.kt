package com.omarahmed.dogsample.model

data class Dog(
    val id: String,
    val image: String,
    val breeds: List<Breed>
)