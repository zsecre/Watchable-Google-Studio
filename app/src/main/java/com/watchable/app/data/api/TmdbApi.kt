package com.watchable.app.data.api

import com.watchable.app.data.model.TmdbMedia
import com.watchable.app.data.model.TmdbResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(@Query("page") page: Int = 1): TmdbResponse<TmdbMedia>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): TmdbResponse<TmdbMedia>

    @GET("trending/tv/day")
    suspend fun getTrendingTv(@Query("page") page: Int = 1): TmdbResponse<TmdbMedia>

    @GET("tv/popular")
    suspend fun getPopularTv(@Query("page") page: Int = 1): TmdbResponse<TmdbMedia>

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): TmdbResponse<TmdbMedia>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/original"
    }
}
