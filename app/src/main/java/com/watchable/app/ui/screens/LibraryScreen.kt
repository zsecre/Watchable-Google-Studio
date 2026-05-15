package com.watchable.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.watchable.app.data.model.Media
import com.watchable.app.data.model.MediaType
import com.watchable.app.ui.components.MediaCard
import com.watchable.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())
    val history by viewModel.history.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Library") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (history.isNotEmpty()) {
                Text(
                    text = "Recently Viewed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(history) { hist ->
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .clickable {
                                    // Mocking Media object from History for click
                                    onMediaClick(
                                        Media(
                                            id = hist.mediaId,
                                            title = hist.title,
                                            posterPath = hist.posterPath,
                                            backdropPath = null,
                                            overview = "",
                                            type = hist.type,
                                            rating = 0.0,
                                            releaseDate = null
                                        )
                                    )
                                }
                        ) {
                            AsyncImage(
                                model = hist.posterPath,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hist.title,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Text(
                text = "Watchlist",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            
            if (watchlist.isEmpty()) {
                Text(
                    text = "Your watchlist is empty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                // Grid layout inside column is tricky with scrolling, but we can use a non-scrolling FlowRow or similar
                // For simplicity, let's just use a Column of Cards or a height-capped LazyVerticalGrid (not recommended)
                // Better: Use LazyColumn for the whole screen and use items for grid-like layout
                watchlist.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { media ->
                            MediaCard(
                                media = media,
                                onClick = { onMediaClick(media) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
