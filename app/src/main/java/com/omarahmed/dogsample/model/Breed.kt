package com.omarahmed.dogsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Breed(
    val id: String,
    val name: String,
    val group: String,
    val lifeSpan: String,
    val temperament: String
) : Parcelable