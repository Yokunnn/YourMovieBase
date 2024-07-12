package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("photo")
    val photoUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("enName")
    val enName: String,
    @SerializedName("profession")
    val profession: String,
    @SerializedName("enProfession")
    val enProfession: String
)
