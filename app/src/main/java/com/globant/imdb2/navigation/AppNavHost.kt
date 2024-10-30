package com.globant.imdb2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.globant.imdb2.MainScreen

import com.globant.imdb2.viewmodel.LoginViewModel

@Composable
fun AppNavHost(
    navController: NavHostController
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val isLoggedIn by rememberSaveable { mutableStateOf( loginViewModel.isLoggedIn()) }

    NavHost(
        navController = navController,
        route = "root_graph",
        startDestination = if (isLoggedIn) "main_graph" else "auth_graph",
    ) {
        authNavGraph(navController)
        composable (route = "main_graph"){
            MainScreen()
        }
    }
}