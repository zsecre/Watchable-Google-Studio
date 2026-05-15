package com.watchable.app.data.repository

import com.watchable.app.data.api.JikanApi
import com.watchable.app.data.api.TmdbApi
import com.watchable.app.data.local.HistoryDao
import com.watchable.app.data.local.MediaDao
import com.watchable.app.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val jikanApi: JikanApi,
    private val mediaDao: MediaDao,
    private val historyDao: HistoryDao
) {
    // Watchlist
    val watchlist = mediaDao.getWatchlist()

    suspend fun toggleWatchlist(media: Media) {
        val existing = mediaDao.getMediaById(media.id)
        if (existing != null) {
            mediaDao.deleteMedia(existing)
        } else {
            mediaDao.insertMedia(media.copy(timestamp = System.currentTimeMillis()))
        }
    }

    fun isInWatchlist(id: String): Flow<Boolean> = mediaDao.isInWatchlist(id)

    // History
    val history = historyDao.getHistory()

    suspend fun addToHistory(media: Media) {
        historyDao.insertHistory(
            History(
                id = "hist_${media.id}",
                mediaId = media.id,
                title = media.title,
                posterPath = media.posterPath,
                type = media.type,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    // TMDB
    suspend fun getTrendingMovies() = tmdbApi.getTrendingMovies().results.map { it.toMedia(MediaType.MOVIE) }
    suspend fun getPopularMovies() = tmdbApi.getPopularMovies().results.map { it.toMedia(MediaType.MOVIE) }
    suspend fun getNowPlayingMovies() = tmdbApi.getTrendingMovies().results.map { it.toMedia(MediaType.MOVIE) } // Mocking for now or use specific endpoint if available

    suspend fun getTrendingTv() = tmdbApi.getTrendingTv().results.map { it.toMedia(MediaType.TV) }
    suspend fun getPopularTv() = tmdbApi.getPopularTv().results.map { it.toMedia(MediaType.TV) }

    suspend fun searchTmdb(query: String) = tmdbApi.searchMulti(query).results
        .filter { it.title != null || it.name != null }
        .map { it.toMedia(if (it.title != null) MediaType.MOVIE else MediaType.TV) }

    // Anime
    suspend fun getTopAnime() = jikanApi.getTopAnime().data.map { it.toMedia() }
    suspend fun getSeasonNow() = jikanApi.getSeasonNow().data.map { it.toMedia() }
    suspend fun searchAnime(query: String) = jikanApi.searchAnime(query).data.map { it.toMedia() }

    private fun TmdbMedia.toMedia(type: MediaType) = Media(
        id = id.toString(),
        title = title ?: name ?: "Unknown",
        posterPath = posterPath?.let { "${TmdbApi.IMAGE_BASE_URL}$it" },
        backdropPath = backdropPath?.let { "${TmdbApi.BACKDROP_BASE_URL}$it" },
        overview = overview,
        type = type,
        rating = rating,
        releaseDate = releaseDate ?: firstAirDate
    )

    private fun JikanAnime.toMedia() = Media(
        id = "anime_$id",
        title = title,
        posterPath = images.jpg.largeImageUrl,
        backdropPath = null,
        overview = synopsis ?: "",
        type = MediaType.ANIME,
        rating = score ?: 0.0,
        releaseDate = aired?.string
    )
}
