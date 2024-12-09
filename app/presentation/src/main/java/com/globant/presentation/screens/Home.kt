package com.globant.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.globant.domain.model.Movie
import com.globant.presentation.R
import com.globant.presentation.theme.Grey
import com.globant.presentation.theme.LightGray
import com.globant.presentation.theme.Yellow
import com.globant.presentation.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewmodel: MainViewModel = hiltViewModel()){
    Column (verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)) {

        val items = viewmodel.movies.observeAsState()

        val loaded = viewmodel.dataFetched.observeAsState()

        LaunchedEffect(key1 = Unit) {
            viewmodel.loadMovies()
        }

        if(loaded.value == false){
            Box(modifier = Modifier.fillMaxSize()){
                Animation(modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center))
            }
        }else {
            items.value?.let {
                Trailer(movie = it.first())
                Carousel(title = "La mejor selección", movies = it.drop(1), navController)
            }
        }
    }
}

@Composable
fun Animation(modifier: Modifier){
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animationload)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}



@Composable
fun Carousel(title: String, movies: List<com.globant.domain.model.Movie>, navController: NavController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically){

            Box(
                modifier = Modifier
                    .size(8.dp, 35.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Yellow)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        LazyRow(modifier = Modifier
            .padding(top = 15.dp, bottom = 30.dp, start = 20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(movies) {movie ->
                MovieCard(movie = movie, navController = navController)

            }
        }

    }
}


@Composable
fun Trailer(movie: Movie){
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(Color.White)) {
        val (trailer, poster, info) = createRefs()

        Box(modifier = Modifier
            .constrainAs(trailer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }){
            Log.d("HERE", movie.toString())
            GlideImage(imageModel = "https://image.tmdb.org/t/p/w500${movie.backImage}" ,
                contentScale = ContentScale.Crop,
                contentDescription = "movie trailer",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Box(modifier = Modifier
            .padding(bottom = 30.dp, start = 20.dp)
            .constrainAs(poster) {
                bottom.linkTo(parent.bottom)
            }){
            GlideImage(imageModel = "https://image.tmdb.org/t/p/w500" + movie.image,
                contentScale = ContentScale.Crop,
                contentDescription = "movie poster",
                modifier = Modifier
                    .height(180.dp)
                    .width(130.dp)
                    //.background(Color.Red)
                    //.padding(top = 50.dp, start = 20.dp)
                    )
        }


        Column(modifier =
        Modifier
            .padding(bottom = 50.dp, start = 10.dp)
            .fillMaxWidth()
            .constrainAs(info) {
                start.linkTo(poster.end)
                bottom.linkTo(poster.bottom)
            }){

            Text(text=movie.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
            Text(text = "Trailer oficial")

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(movie: Movie, navController: NavController){
    Card(onClick = {
        navController.navigate("detail/${movie.id}")},
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 5.dp, bottomStart = 5.dp), elevation = 5.dp,
        modifier = Modifier
            .height(240.dp)
            .width(130.dp)){
        Box(modifier = Modifier.fillMaxSize()){

            Column(modifier = Modifier.fillMaxWidth()){

                GlideImage(imageModel = "https://image.tmdb.org/t/p/w500" + movie.image,
                    contentScale = ContentScale.Crop,
                    contentDescription = "movie poster",
                    modifier = Modifier
                        .height(180.dp)
                        .width(130.dp))

                /*Image(
                    painter = painterResource(R.drawable.portada),
                    contentScale = ContentScale.Crop,
                    contentDescription = "movie poster",
                    modifier = Modifier
                        .height(220.dp)
                        .width(160.dp)
                )*/

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = R.drawable.icon_material_stars),
                        tint = Yellow,
                        contentDescription = "star",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(14.dp)
                    )

                    Text(
                        text = String.format(Locale.ROOT,"%.1f", movie.score),
                        fontSize = 12.sp,
                        color = Grey
                    )
                }

                Text(
                    text = movie.name,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                )
            }

            Icon(imageVector = Icons.Filled.Info,
                contentDescription = "info",
                tint = Color.LightGray,
                modifier = Modifier
                    .padding(5.dp)
                    .size(18.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}