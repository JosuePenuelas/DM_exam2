package com.example.dm_exam2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dm_exam2.ui.screens.FavoritesScreen
import com.example.dm_exam2.ui.screens.HomeScreen
import com.example.dm_exam2.ui.screens.ShowDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onShowClick = { showId ->
                    navController.navigate("detail/$showId")
                },
                onFavoritesClick = {
                    navController.navigate("favorites")
                }
            )
        }
        composable("detail/{showId}") { backStackEntry ->
            val showId = backStackEntry.arguments?.getString("showId")?.toIntOrNull()
            showId?.let {
                ShowDetailScreen(navController, it)
            }
        }
        composable("favorites") {
            FavoritesScreen(
                navController = navController,
                onShowClick = { showId ->
                    navController.navigate("detail/$showId")
                })
        }
    }
}
