package com.watchable.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.watchable.app.data.model.Media
import com.watchable.app.ui.screens.*
import com.watchable.app.ui.theme.WatchableTheme
import com.watchable.app.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchableTheme {
                MainLayout()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Movies : Screen("movies", "Movies", Icons.Default.Movie)
    object TvAnime : Screen("tvanime", "TV & Anime", Icons.Default.Tv)
    object Library : Screen("library", "Library", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Detail : Screen("detail/{mediaJson}", "Detail", Icons.Default.Home)
}

@Composable
fun MainLayout() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val mainScreens = listOf(Screen.Home, Screen.Movies, Screen.TvAnime, Screen.Library, Screen.Profile)
            if (currentRoute in mainScreens.map { it.route }) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    mainScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel) { media ->
                    navigateToDetail(navController, media)
                }
            }
            composable(Screen.Movies.route) {
                MoviesScreen(viewModel) { media ->
                    navigateToDetail(navController, media)
                }
            }
            composable(Screen.TvAnime.route) {
                TvAnimeScreen(viewModel) { media ->
                    navigateToDetail(navController, media)
                }
            }
            composable(Screen.Library.route) {
                LibraryScreen(viewModel) { media ->
                    navigateToDetail(navController, media)
                }
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel)
            }
            composable(Screen.Search.route) {
                SearchScreen(viewModel) { media ->
                    navigateToDetail(navController, media)
                }
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("mediaJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("mediaJson")
                val decodedJson = URLDecoder.decode(json ?: "", "UTF-8")
                val media = Gson().fromJson(decodedJson, Media::class.java)
                DetailScreen(media, viewModel) {
                    navController.popBackStack()
                }
            }
        }
        
        // Overlay Search icon on Home
        if (currentRoute == Screen.Home.route) {
            Box(modifier = Modifier.padding(padding)) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate(Screen.Search.route) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    }
}

private fun navigateToDetail(navController: androidx.navigation.NavController, media: Media) {
    val json = Gson().toJson(media)
    val encodedJson = URLEncoder.encode(json, "UTF-8")
    navController.navigate("detail/$encodedJson")
}
