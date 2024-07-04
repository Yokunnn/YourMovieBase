package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.data.model.SingleMovieResponse

interface MovieDetailsService {

    @GET(GET_MOVIE_BY_ID_REQUEST)
    suspend fun getMovieById(
        @Query(QUERY_ID) id: Int
    ): SingleMovieResponse

    companion object {
        private const val GET_MOVIE_BY_ID_REQUEST = "/v1.4/movie/{id}"
        private const val QUERY_ID = "id"
    }
}