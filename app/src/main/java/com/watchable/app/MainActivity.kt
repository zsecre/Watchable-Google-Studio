package com.watchable.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
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
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Library : Screen("library", "Library", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
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
            if (currentRoute != Screen.Home.route && currentRoute != Screen.Search.route && currentRoute != Screen.Library.route && currentRoute != Screen.Profile.route) {
                // Hide bottom bar on detail screen or others
            } else {
                NavigationBar {
                    val items = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Profile)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
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
            composable(Screen.Search.route) {
                SearchScreen(viewModel) { media ->
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
    }
}

private fun navigateToDetail(navController: androidx.navigation.NavController, media: Media) {
    val json = Gson().toJson(media)
    val encodedJson = URLEncoder.encode(json, "UTF-8")
    navController.navigate("detail/$encodedJson")
}
