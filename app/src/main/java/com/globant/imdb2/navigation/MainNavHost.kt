package com.globant.imdb2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.globant.imdb2.screens.DetailScreen
import com.globant.imdb2.screens.HomeScreen
import com.globant.imdb2.screens.ProfileScreen
import com.globant.imdb2.screens.SearchScreen

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier){

    NavHost(navController = navController,
        startDestination = "Home",
        route = "main_graph",
        modifier = modifier
    ) {
        composable(route = "Home") {
            HomeScreen(navController)
        }
        composable(route = "Profile") {
            ProfileScreen()
        }
        composable(route = "Search") {
            SearchScreen(navController = navController)
        }
        composable(route = "detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")!!
            DetailScreen(navController, movieId)
        }
    }
}