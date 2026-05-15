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
fun MoviesScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    val popularMovies by viewModel.popularMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(popularMovies) { media ->
                MediaCard(
                    media = media,
                    onClick = { onMediaClick(media) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
