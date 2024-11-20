package com.globant.imdb2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.globant.imdb2.presentation.screens.DetailScreen
import com.globant.imdb2.presentation.screens.HomeScreen
import com.globant.imdb2.presentation.screens.ProfileScreen
import com.globant.imdb2.presentation.screens.SearchScreen

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier, logout: ()->Unit){

    NavHost(navController = navController,
        startDestination = "Home",
        route = "main_graph",
    ) {
        composable(route = "Home") {
            HomeScreen(navController)
        }
        composable(route = "Profile") {
            ProfileScreen(logout = logout)
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