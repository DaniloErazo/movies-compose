package com.globant.imdb2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.globant.imdb2.R
import com.globant.imdb2.model.Movie
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun DetailScreen(){
    val movie = Movie(id = "1", name = "Gambito de dama", image = "", 4.5, originalTitle = "The Queens Gambit", description = "Ambientada en la Guerra Fría; una huérfana llamada Beth Harmon con un don para el ajedrez lucha contra las adicciones mientras… intenta ser la mejor jugadora del mundo.")

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        movie.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )

        }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .background(colorResource(id = R.color.light_grey))){
            Title(title = movie.name, original = movie.originalTitle )

            GlideImage(imageModel = "",
                contentScale = ContentScale.Crop,
                contentDescription = "movie trailer",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .height(250.dp)
            )
            MovieDetail()

            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()){

                Divider(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))

                Text(text = "Guía de episodios: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(20.dp))

                Icon(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .size(30.dp),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = "",
                    tint = colorResource(id = R.color.dark_grey)
                )

                Divider(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter))

            }


            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), contentAlignment = Alignment.Center){

                Button(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(top=15.dp, bottom = 15.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.yellow))
                ) {
                    Text(text = "Agregar a mi lista de seguimiento",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Subtitle(text = "Director: ")

            Subtitle(text = "Escritores: ")

            Subtitle(text = "Reparto y producción de la serie")
        }
    }

}

@Composable
fun Subtitle(text: String){
    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
        .fillMaxWidth()){

        Divider(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))

        Text(text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(20.dp))

        Icon(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 10.dp)
            .size(30.dp),
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = "",
            tint = colorResource(id = R.color.dark_grey))

    }
}

@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun MovieDetail(){

    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 3.dp)
        .height(200.dp)) {

        val (image, text, info) = createRefs()

        GlideImage(imageModel = "",
            contentScale = ContentScale.Crop,
            contentDescription = "movie poster",
            modifier = Modifier
                .width(130.dp)
                .height(150.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 20.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier
            .height(150.dp)
            .padding(start = 20.dp, end = 20.dp)
            .constrainAs(text) {
                start.linkTo(image.end)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text="Drama",
                    color = colorResource(id = R.color.dark_grey),
                    modifier = Modifier
                        .border(
                            1.dp,
                            colorResource(id = R.color.dark_grey),
                            RoundedCornerShape(5.dp)
                        )
                        .padding(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 5.dp)){
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "star icon", tint = colorResource(
                        id = R.color.yellow
                    ))
                    Text(text= "4.5", color = colorResource(id = R.color.dark_grey))
                }
            }
            Text(text = "Ambientada en la Guerra Fría; una huérfana llamada Beth Harmon con un don para el ajedrez lucha contra las adicciones mientras… intenta ser la mejor jugadora del mundo.")

        }

    }

}

@Composable
fun Title(title: String, original: String){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(bottom = 15.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp, 35.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = colorResource(R.color.yellow))
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Text(text = original,
            fontSize = 14.sp,
            color = colorResource(id = R.color.dark_grey),
            modifier = Modifier.padding(start = 40.dp, bottom = 10.dp))

        Text(text = "Miniserie de TV 2020 - 2020 -16",
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_grey),
            modifier = Modifier.padding(start = 40.dp))
    }
}