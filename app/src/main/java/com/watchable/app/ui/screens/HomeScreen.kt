package com.watchable.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.watchable.app.data.model.Media
import com.watchable.app.ui.components.MediaRow
import com.watchable.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val trendingTv by viewModel.trendingTv.collectAsState()
    val topAnime by viewModel.topAnime.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Watchable", color = MaterialTheme.colorScheme.primary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                MediaRow(
                    title = "Trending Movies",
                    items = trendingMovies,
                    onMediaClick = onMediaClick
                )
            }
            item {
                MediaRow(
                    title = "Popular Movies",
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
                    title = "Top Anime",
                    items = topAnime,
                    onMediaClick = onMediaClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
