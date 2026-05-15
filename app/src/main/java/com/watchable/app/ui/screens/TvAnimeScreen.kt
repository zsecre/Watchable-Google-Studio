package com.watchable.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.watchable.app.data.model.Media
import com.watchable.app.ui.components.MediaCard
import com.watchable.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvAnimeScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val trendingTv by viewModel.trendingTv.collectAsState()
    val topAnime by viewModel.topAnime.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("TV & Anime") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("TV Shows") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Anime") }
                    )
                }
            }
        }
    ) { padding ->
        val items = if (selectedTab == 0) trendingTv else topAnime
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items) { media ->
                MediaCard(
                    media = media,
                    onClick = { onMediaClick(media) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
