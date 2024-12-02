package com.globant.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.globant.presentation.screens.SignIn
import com.globant.presentation.screens.SignUp

fun NavGraphBuilder.authNavGraph(navController: NavController){
    navigation(
        route = "auth_graph",
        startDestination = "Login"
    ){
        composable(route = "Login") {
            SignIn(navController = navController)
        }

        composable(route = "Register") {
            SignUp(navController = navController)
        }
    }

}
