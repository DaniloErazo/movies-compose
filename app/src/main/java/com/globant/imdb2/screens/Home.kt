package com.globant.imdb2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.globant.imdb2.R
import com.globant.imdb2.model.Movie
import com.skydoves.landscapist.glide.GlideImage

@Composable
@Preview(showBackground = true)
fun HomeScreen(){
    Column (verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.light_grey))) {
        val movie = Movie(id = "1", name = "Gambito de dama", image = "", 4.5, originalTitle = "The Queens Gambit", description = "Ambientada en la Guerra Fría; una huérfana llamada Beth Harmon con un don para el ajedrez lucha contra las adicciones mientras… intenta ser la mejor jugadora del mundo.")
        Trailer()
        Carousel(title = "La mejor selección", movies =  listOf(movie))
    }
}

@Composable
fun Carousel(title: String, movies: List<Movie>){
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), verticalAlignment = Alignment.CenterVertically){

            Box(
                modifier = Modifier
                    .size(8.dp, 35.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = colorResource(R.color.yellow))
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
            .fillMaxWidth()) {
            items(movies) {movie ->
                MovieCard(movie = movie)

            }
        }

    }
}


@Composable
fun Trailer(){
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp).background(Color.White)) {
        val (trailer, poster, info) = createRefs()

        Box(modifier = Modifier
            .background(Color.Red)
            .constrainAs(trailer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }){
            GlideImage(imageModel = "https://m.media-amazon.com/images/I/71TB68pMjpL._AC_SX679_.jpg",
                contentScale = ContentScale.Crop,
                contentDescription = "movie trailer",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Box(modifier = Modifier
            .padding(bottom = 30.dp, start = 20.dp)
            .background(Color.Blue)
            .constrainAs(poster) {
                bottom.linkTo(parent.bottom)
            }){
            GlideImage(imageModel = "https://m.media-amazon.com/images/I/71TB68pMjpL._AC_SX679_.jpg",
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

            Text(text="Stranger Things", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
            Text(text = "Trailer oficial")

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(movie: Movie){
    Card(onClick = { /*TODO*/ },
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 5.dp, bottomStart = 5.dp), elevation = 5.dp,
        modifier = Modifier
            .height(240.dp)
            .width(130.dp)){
        Box(modifier = Modifier.fillMaxSize()){

            Column(modifier = Modifier.fillMaxWidth()){

                GlideImage(imageModel = "https://m.media-amazon.com/images/I/71TB68pMjpL._AC_SX679_.jpg",
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
                        tint = colorResource(id = R.color.yellow),
                        contentDescription = "star",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(14.dp)
                    )

                    Text(
                        text = movie.score.toString(),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.grey)
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