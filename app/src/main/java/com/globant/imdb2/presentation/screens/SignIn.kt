package com.globant.imdb2.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.globant.imdb2.R
import com.globant.imdb2.presentation.viewmodel.LoginViewModel

@Composable
fun SignIn(navController: NavController, viewModel: LoginViewModel = hiltViewModel()){

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.yellow))
    ){
        Image(painter = painterResource(id = R.drawable.imdb),
            contentDescription = "",
            modifier = Modifier
                .size(250.dp)
                .padding(top = 50.dp)
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 50.dp, start = 50.dp)){

            var username by rememberSaveable {
                mutableStateOf("")
            }

            var password by rememberSaveable {
                mutableStateOf("")
            }

            val errorLogin by viewModel.errorLogin.collectAsState()
            val loggedUser by viewModel.loggedUser.observeAsState()

            val context = LocalContext.current

            LaunchedEffect(errorLogin) {
                errorLogin?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }

            LaunchedEffect(loggedUser) {
                loggedUser?.let { state ->
                    if(state.isLogged){
                        navController.navigate("main_graph")
                    }
                }

            }

            InputText(name = "Usuario", input = username ) {username = it}

            InputPassword(input = password) {password = it}

            Text(text = "¿Olvidaste tu contraseña?", fontStyle = FontStyle.Italic, color = colorResource(id = R.color.dark_grey))

            Spacer(modifier = Modifier.size(25.dp))

            val isButttonEnabled = username.isNotEmpty() && password.isNotEmpty()

            Button(text = "Login", isButttonEnabled, onClick = {viewModel.signInUser(username, password)})

            Spacer(modifier = Modifier.size(25.dp))

            Text(color = colorResource(id = R.color.dark_grey),
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                text = "O podés ingresar con:",
                textAlign = TextAlign.Center ,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.Center,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp)){

                RoundedButton(icon = painterResource(id = R.drawable.google))
                Spacer(modifier = Modifier.size(5.dp))
                RoundedButton(icon = painterResource(id = R.drawable.facebook))
                Spacer(modifier = Modifier.size(5.dp))
                RoundedButton(icon = painterResource(id = R.drawable.apple))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(color = colorResource(id = R.color.dark_grey),
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    text = "No tienes cuenta? ",
                    textAlign = TextAlign.Center
                )
                Text(color = colorResource(id = R.color.dark_grey),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Regístrate",
                    textAlign = TextAlign.Center ,
                    modifier = Modifier.clickable { navController.navigate("Register") }
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(color = colorResource(id = R.color.dark_grey),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = "Continuar como invitado",
                textAlign = TextAlign.Center ,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("main_graph") }
            )

        }
    }
}

@Composable
fun InputPassword(input: String, onChange:(String)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "Contraseña",
            color = colorResource(id = R.color.dark_grey),
            fontSize = 16.sp, fontWeight = FontWeight.Bold
        )

        Column {
            TextField( value = input,
                onValueChange = {
                    onChange(it)
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp)
            )
        }
    }

}

@Composable
fun InputText(name: String, input:String, onChange:(String)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){

        Text(text = name,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.dark_grey),
            fontSize = 16.sp)

        TextField( value = input,
            onValueChange = {
                onChange(it)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedTextColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        )

    }
}

@Composable
fun Button(text: String, enabled: Boolean, onClick: () -> Unit){
    Button(onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = enabled,
        contentPadding = PaddingValues(top=20.dp, bottom = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.grey), disabledContainerColor = colorResource(
            id = R.color.medium_grey
        ))
    ) {
        Text(text = text,
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun RoundedButton(icon: Painter){

    IconButton(onClick = { /*TODO*/ },
        colors = IconButtonDefaults.iconButtonColors(Color.White),
        modifier = Modifier.size(70.dp)) {

        Image(painter = icon,
            contentDescription = "",
            modifier = Modifier.size(40.dp))
        
    }
    
}
