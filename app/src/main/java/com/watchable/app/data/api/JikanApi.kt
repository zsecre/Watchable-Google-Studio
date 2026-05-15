package com.watchable.app.data.api

import com.watchable.app.data.model.JikanAnime
import com.watchable.app.data.model.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApi {
    @GET("top/anime")
    suspend fun getTopAnime(@Query("page") page: Int = 1): JikanResponse<JikanAnime>

    @GET("seasons/now")
    suspend fun getSeasonNow(@Query("page") page: Int = 1): JikanResponse<JikanAnime>

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): JikanResponse<JikanAnime>

    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
    }
}
