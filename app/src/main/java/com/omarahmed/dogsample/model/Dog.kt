package com.omarahmed.dogsample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(
    val id: String,
    val image: String,
    val breed: Breed
) : Parcelable