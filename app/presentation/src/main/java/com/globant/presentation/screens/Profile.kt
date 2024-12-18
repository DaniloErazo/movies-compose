package com.globant.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globant.presentation.theme.DarkGray
import com.globant.presentation.theme.LightGray
import com.globant.presentation.theme.Yellow
import com.globant.presentation.viewmodel.LoginViewModel

@Composable
fun ProfileScreen(viewModel: LoginViewModel = hiltViewModel(), logout: () -> Unit){

    val loggedUser by viewModel.loggedUser.observeAsState()
    val user = loggedUser?.user

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }


    Column (modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 100.dp)
        .verticalScroll(rememberScrollState())
        .background(LightGray), verticalArrangement = Arrangement.spacedBy(15.dp)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)){

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), contentAlignment = Alignment.CenterStart){

                val colorValue = user?.color ?: android.graphics.Color.argb(255, 231, 200, 150)

                Row(modifier = Modifier.align(
                    Alignment.CenterStart), verticalAlignment = Alignment.CenterVertically){
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(BorderStroke(0.3.dp, Color.Black), CircleShape)
                            .clip(CircleShape)
                            .background(Color(colorValue)),
                        contentAlignment = Alignment.Center
                    ) {
                        val capitalLetter = user?.name?.first()?.uppercaseChar()?.toString() ?: "B"
                        Text(
                            text = capitalLetter ,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp
                        )
                    }
                    /*
                    Image(painter = painterResource(id = R.drawable.profilepic),
                        contentDescription = "profile image",
                        modifier = Modifier
                            .size(50.dp)
                            .border(0.3.dp, Color.Black, CircleShape)
                            .clip(
                                CircleShape
                            ))

                     */

                    Text(text = user?.name ?: "Invitado", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start= 10.dp))
                }
                Icon(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp),
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "",
                    tint = Yellow)

            }

            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
            )

            val card1 = hashMapOf("name" to "Calificaciones", "description" to "Calificar y obtener recomendaciones", "counter" to "0" )
            val card2 = hashMapOf("name" to "Listas", "description" to "Agregar a listas", "counter" to "4" )
            val card3 = hashMapOf("name" to "Comentarios", "description" to "Aún no hay críticas", "counter" to "0" )

            val cardsList = listOf(card1, card2, card3)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                cardsList.map { card ->
                    SimpleCard(cardName = card["name"].toString(), counter = card["counter"].toString() , description = card["description"].toString() )
                }
            }




        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)){
            Title(title = "Lista de seguimiento")

            Text(modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = "Crea una lista de seguimiento para no perderte ninguna película o serie de TV",
                fontSize = 16.sp
            )

            BigButton(text = "Empieza tu lista de seguimiento", {})
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)){
            Title(title = "Vistas recientes")
            Text(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                fontSize = 16.sp,
                text = "No has visitado ninguna página recientemente")
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)){
            Title(title = "Personas favoritas")

            Text(modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = "Añadir actores y directores favoritos y más para conocer las últimas novedades",
                fontSize = 16.sp
            )

            BigButton(text = "Agregar personas favoritas", {})
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)){

            Subtitle(text = "Cines favoritos")
            Subtitle(text = "Estaciones")
            Subtitle(text = "Notificaciones")
            Subtitle(text = "Mejorar selecciones principales")

        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)) {

            BigButton(text = "Log Out") {
                viewModel.logOutCurrentUser()
                logout()
            }
        }

    }
}

@Composable
fun SimpleCard(cardName: String, counter: String, description: String){

    Card(modifier = Modifier
        .height(150.dp)
        .width(160.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    LightGray
                )){
                Text(text = description, textAlign = TextAlign.Start, modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .fillMaxWidth())
            }


            Text(text = cardName)

            Text(text = counter, color = DarkGray)
        }

    }

}

@Composable
fun BigButton(text: String, onClick: () ->Unit){
    Button(onClick = { onClick() },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(top=20.dp, bottom = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(Yellow)
    ) {
        Text(text = text,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )
    }

}

@Composable
fun Title(title: String){
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
                    .background(color = Yellow)
            )
            Text(
                text = title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

    }
}