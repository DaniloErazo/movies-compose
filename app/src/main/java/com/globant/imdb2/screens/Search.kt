package com.globant.imdb2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.globant.imdb2.R
import com.globant.imdb2.viewmodel.SearchScreenViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewmodel: SearchScreenViewModel = hiltViewModel(), navController: NavController){

    val filteredItems by viewmodel.filtered.observeAsState(emptyList())

    Column (modifier = Modifier.fillMaxSize()) {

        var query by rememberSaveable {
            mutableStateOf("")
        }

        var active by rememberSaveable {
            mutableStateOf(false)
        }

        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp, start = 10.dp),
            query = query,
            onQueryChange = { query = it
                            viewmodel.filterMovies(query) },
            onSearch = {active = false
                viewmodel.filterMovies(query)},
            active = active ,
            onActiveChange = {active = it},
            placeholder = {Text("Busca una película")},
            leadingIcon = {Icon(Icons.Default.Search, contentDescription = null)}
        ) {

        }


        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
            items(filteredItems){ movie ->
                MovieCard(image = movie.movieImage, title = movie.movieName, date = movie.movieDate, id = movie.identifier, navController)
            }

        }
    }
}

@Composable
fun MovieCard(image:String, title: String, date: String, id: String, navController: NavController){

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(colorResource(id = R.color.light_grey))
        .clickable { navController.navigate("detail/${id}")  }
        .padding(10.dp)){

        GlideImage(imageModel = "https://image.tmdb.org/t/p/w500$image",
            contentScale = ContentScale.Crop,
            contentDescription = "movie trailer",
            modifier = Modifier
                .height(150.dp)
                .width(100.dp)
                .padding(10.dp)
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp), verticalArrangement = Arrangement.Top){
            Text(text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp))
            Text(text = date)
        }

    }
}

