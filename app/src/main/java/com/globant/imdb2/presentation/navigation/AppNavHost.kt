package com.globant.imdb2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.globant.imdb2.presentation.MainScreen


@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = "root_graph",
        startDestination = "Loading",
    ) {
        authNavGraph(navController)
        composable(route = "Loading") {
            LoadingScreen(
                onLogged = {
                    navController.navigate("main_graph") {
                        popUpTo("Loading") {
                            inclusive = true  // prevent going back to LoadingScreen
                        }
                    }
                },
                onNotLogged = {
                    navController.navigate("auth_graph") {
                        popUpTo("Loading") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable (route = "main_graph"){
            MainScreen(logout = {
                navController.navigate("auth_graph") {
                    popUpTo(0){}
                }})
        }
    }
}