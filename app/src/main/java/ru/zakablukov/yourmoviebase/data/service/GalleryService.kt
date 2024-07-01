package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.data.model.MoviesResponse

interface GalleryService {

    @GET("v1.4/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("notNullFields") notNullFields: List<String> = listOf(
            "alternativeName",
            "poster.url"
        ),
        @Query("sortField") sortField: List<String> = listOf(
            "rating.imdb",
            "rating.kp"
        ),
        @Query("sortType") sortType: List<String> = listOf(
            "-1",
            "-1"
        ),
        @Query("type") type: List<String> = listOf(
            "movie"
        )
    ): MoviesResponse
}