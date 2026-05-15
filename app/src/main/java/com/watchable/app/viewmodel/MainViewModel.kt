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

    private val _topAnime = MutableStateFlow<List<Media>>(emptyList())
    val topAnime = _topAnime.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Media>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val watchlist = repository.watchlist

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            try {
                _trendingMovies.value = repository.getTrendingMovies()
                _popularMovies.value = repository.getPopularMovies()
                _trendingTv.value = repository.getTrendingTv()
                _topAnime.value = repository.getTopAnime()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
