package com.globant.imdb2

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.compose.rememberNavController
import com.globant.imdb2.screens.BottomNavigationItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            icon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Buscar",
            selectedIcon = Icons.Filled.Search,
            icon = Icons.Outlined.Search
        ),
        BottomNavigationItem(
            title = "Play",
            selectedIcon = Icons.Filled.PlayArrow,
            icon = Icons.Outlined.PlayArrow
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            icon = Icons.Outlined.Person
        ))

    var selectedScreen by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {

        Scaffold (bottomBar = {
            NavigationBar(containerColor = colorResource(id = R.color.yellow), modifier = Modifier.clip(RoundedCornerShape(45.dp, 45.dp, 0.dp, 0.dp))) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedScreen == index,
                        onClick = { selectedScreen = index
                            navController.navigate(item.title)
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
        }) {

        }

    }

}