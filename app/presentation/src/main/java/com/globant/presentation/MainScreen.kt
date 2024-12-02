package com.globant.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.globant.presentation.theme.Yellow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(logout: () -> Unit){

    val items = listOf(
        com.globant.presentation.navigation.BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            icon = Icons.Outlined.Home
        ),
        com.globant.presentation.navigation.BottomNavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            icon = Icons.Outlined.Search
        ),
        com.globant.presentation.navigation.BottomNavigationItem(
            title = "Play",
            selectedIcon = Icons.Filled.PlayArrow,
            icon = Icons.Outlined.PlayArrow
        ),
        com.globant.presentation.navigation.BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            icon = Icons.Outlined.Person
        )
    )

    var selectedScreen by rememberSaveable {
        mutableIntStateOf(0)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()

        Scaffold (bottomBar = {
            NavigationBar(containerColor = Yellow, modifier = Modifier.clip(RoundedCornerShape(45.dp, 45.dp, 0.dp, 0.dp))) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedScreen == index,
                        onClick = { selectedScreen = index
                            navController.navigate(item.title){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                                    },
                        label = { Text(text = item.title) } ,
                        icon = {
                            Icon(
                                imageVector = if(selectedScreen == index) {item.selectedIcon} else {item.icon},
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        }) { innerPadding ->
            com.globant.presentation.navigation.MainNavHost(
                navController = navController,
                Modifier.padding(innerPadding),
                logout
            )

        }

    }

}
