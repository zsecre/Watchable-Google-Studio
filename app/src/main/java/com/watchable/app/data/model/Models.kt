package com.watchable.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "watchlist")
data class Media(
    @PrimaryKey val id: String,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String,
    val type: MediaType,
    val rating: Double,
    val releaseDate: String?,
    val genres: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

enum class MediaType {
    MOVIE, TV, ANIME
}

// TMDB Response Models
data class TmdbResponse<T>(
    val results: List<T>
)

data class TmdbMedia(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    val overview: String
)

// Jikan (Anime) Response Models
data class JikanResponse<T>(
    val data: List<T>
)

data class JikanAnime(
    @SerializedName("mal_id") val id: Int,
    val title: String,
    val images: JikanImages,
    val score: Double?,
    val synopsis: String?,
    val aired: JikanAired?
)

data class JikanImages(
    val jpg: JikanImageDetails
)

data class JikanImageDetails(
    @SerializedName("large_image_url") val largeImageUrl: String
)

data class JikanAired(
    val string: String?
)
