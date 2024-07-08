package ru.zakablukov.yourmoviebase.presentation.util

object TextUtils {
    fun getAgeRatingString(ageRating: Int?): String = "$ageRating+"

    fun getRatingString(rating: Double?): String = "$rating/10 IMDb"

    fun getLengthString(length: Int?): String = "${length?.div(60)}h ${length?.mod(60)}min"
}