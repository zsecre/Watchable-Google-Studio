package com.watchable.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.watchable.app.data.model.Media
import com.watchable.app.ui.components.HeroBanner
import com.watchable.app.ui.components.MediaRow
import com.watchable.app.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val trendingTv by viewModel.trendingTv.collectAsState()
    val topAnime by viewModel.topAnime.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HeroBanner(
                items = trendingMovies.take(5),
                onMediaClick = onMediaClick
            )
        }
        item {
            MediaRow(
                title = "Popular on Watchable",
                items = popularMovies,
                onMediaClick = onMediaClick
            )
        }
        item {
            MediaRow(
                title = "Trending TV Shows",
                items = trendingTv,
                onMediaClick = onMediaClick
            )
        }
        item {
            MediaRow(
                title = "Top Anime Picks",
                items = topAnime,
                onMediaClick = onMediaClick
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
