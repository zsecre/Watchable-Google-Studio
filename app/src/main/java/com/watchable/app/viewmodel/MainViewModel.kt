package com.watchable.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.watchable.app.data.model.Media
import com.watchable.app.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MediaRepository
) : ViewModel() {

    private val _trendingMovies = MutableStateFlow<List<Media>>(emptyList())
    val trendingMovies = _trendingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<Media>>(emptyList())
    val popularMovies = _popularMovies.asStateFlow()

    private val _trendingTv = MutableStateFlow<List<Media>>(emptyList())
    val trendingTv = _trendingTv.asStateFlow()

    private val _popularTv = MutableStateFlow<List<Media>>(emptyList())
    val popularTv = _popularTv.asStateFlow()

    private val _topAnime = MutableStateFlow<List<Media>>(emptyList())
    val topAnime = _topAnime.asStateFlow()

    private val _nowPlayingAnime = MutableStateFlow<List<Media>>(emptyList())
    val nowPlayingAnime = _nowPlayingAnime.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Media>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val watchlist = repository.watchlist
    val history = repository.history

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            try {
                val trending = repository.getTrendingMovies()
                _trendingMovies.value = trending.ifEmpty { mockMovies() }
                
                val popular = repository.getPopularMovies()
                _popularMovies.value = popular.ifEmpty { mockMovies() }
                
                val tvTrending = repository.getTrendingTv()
                _trendingTv.value = tvTrending.ifEmpty { mockTv() }
                
                val tvPopular = repository.getPopularTv()
                _popularTv.value = tvPopular.ifEmpty { mockTv() }
                
                val animeTop = repository.getTopAnime()
                _topAnime.value = animeTop.ifEmpty { mockAnime() }
                
                val animeSeason = repository.getSeasonNow()
                _nowPlayingAnime.value = animeSeason.ifEmpty { mockAnime() }
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to mocks on error
                _trendingMovies.value = mockMovies()
                _popularMovies.value = mockMovies()
                _trendingTv.value = mockTv()
                _popularTv.value = mockTv()
                _topAnime.value = mockAnime()
                _nowPlayingAnime.value = mockAnime()
            }
        }
    }

    private fun mockMovies() = listOf(
        Media("m1", "The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp9QmSbmI94ST2fzwm.jpg", null, "Batman faces the Joker.", com.watchable.app.data.model.MediaType.MOVIE, 9.0, "2008"),
        Media("m2", "Inception", "https://image.tmdb.org/t/p/w500/edv5CZvj0H9ZubSjS69rZFa7Yoj.jpg", null, "Dreams within dreams.", com.watchable.app.data.model.MediaType.MOVIE, 8.8, "2010"),
        Media("m3", "Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6EwfVDxCzs25vubp2FA.jpg", null, "Space travel.", com.watchable.app.data.model.MediaType.MOVIE, 8.6, "2014")
    )

    private fun mockTv() = listOf(
        Media("t1", "Breaking Bad", "https://image.tmdb.org/t/p/w500/ztkUQvBZ68vS9O9690YvS9FpXh.jpg", null, "Chemistry teacher cooks.", com.watchable.app.data.model.MediaType.TV, 9.5, "2008"),
        Media("t2", "Stranger Things", "https://image.tmdb.org/t/p/w500/x2LSRm2RMAvVLogGbvYGgh7GX68.jpg", null, "Kids and monsters.", com.watchable.app.data.model.MediaType.TV, 8.7, "2016")
    )

    private fun mockAnime() = listOf(
        Media("a1", "Attack on Titan", "https://cdn.myanimelist.net/images/anime/10/47347.jpg", null, "Giants eat people.", com.watchable.app.data.model.MediaType.ANIME, 9.1, "2013"),
        Media("a2", "One Piece", "https://cdn.myanimelist.net/images/anime/6/73245.jpg", null, "Pirate king.", com.watchable.app.data.model.MediaType.ANIME, 8.9, "1999")
    )

    fun addToHistory(media: Media) {
        viewModelScope.launch {
            repository.addToHistory(media)
        }
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                val tmdb = repository.searchTmdb(query)
                val anime = repository.searchAnime(query)
                _searchResults.value = tmdb + anime
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleWatchlist(media: Media) {
        viewModelScope.launch {
            repository.toggleWatchlist(media)
        }
    }

    fun isInWatchlist(id: String) = repository.isInWatchlist(id)
}
