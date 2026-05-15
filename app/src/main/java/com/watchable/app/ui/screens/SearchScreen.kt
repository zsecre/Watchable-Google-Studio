package com.watchable.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.watchable.app.data.model.Media
import com.watchable.app.ui.components.MediaCard
import com.watchable.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    onMediaClick: (Media) -> Unit
) {
    var query by remember { mutableStateFlowOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.search(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search Movies, TV, Anime...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(searchResults) { media ->
                MediaCard(
                    media = media,
                    onClick = { onMediaClick(media) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun <T> mutableStateFlowOf(value: T) = mutableStateOf(value)
