package com.omarahmed.dogsample.data

import com.google.gson.annotations.SerializedName


data class RemoteDog(
    @SerializedName("id")
    val id: String?,

    @SerializedName("url")
    val image: String?,

    @SerializedName("breeds")
    val breeds: List<RemoteBreed>?
)

data class RemoteBreed(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("breed_group")
    val group: String?,

    @SerializedName("life_span")
    val lifeSpan: String?,

    @SerializedName("temperament")
    val temperament: String?
)