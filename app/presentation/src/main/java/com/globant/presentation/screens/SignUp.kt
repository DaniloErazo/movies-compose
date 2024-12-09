package com.globant.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.globant.presentation.R
import com.globant.presentation.theme.DarkGray
import com.globant.presentation.viewmodel.LoginViewModel

@Composable
fun SignUp(navController: NavController, viewModel: LoginViewModel = hiltViewModel()){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(end = 30.dp, start = 30.dp)){

        Image(painter = painterResource(id = R.drawable.imdb),
            contentDescription = "",
            modifier = Modifier
                .size(140.dp)
        )

        Text(color = DarkGray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            text = "Crear una cuenta"
        )
        
        Spacer(modifier = Modifier.size(30.dp))

        var name by rememberSaveable {
            mutableStateOf("")
        }

        var email by rememberSaveable {
            mutableStateOf("")
        }

        var password by rememberSaveable {
            mutableStateOf("")
        }

        var passwordVisible by rememberSaveable {
            mutableStateOf(false)
        }

        InputTextBorder(name = "Nombre", input = name ) {name = it}
        InputTextBorder(name = "Correo electrónico", input = email ) {email = it}
        InputPasswordBorder(name = "Contraseña",
            input = password,
            visibility = passwordVisible,
            onChange = {password = it},
            onVisibileChange = {passwordVisible = !passwordVisible})

        val isButtonEnabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
        Spacer(modifier = Modifier.size(20.dp))

        Button(text = "Aceptar", isButtonEnabled, onClick = {viewModel.signUpUser(email, name, password)})

        val errorLogin by viewModel.errorLogin.observeAsState()
        val loggedUser by viewModel.loggedUser.observeAsState()

        val context = LocalContext.current

        LaunchedEffect(errorLogin) {
            errorLogin?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(loggedUser) {
            loggedUser?.let { state ->
                if(state.isLogged){
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("main_graph")
                }
            }

        }

    }
}

@Composable
fun InputTextBorder(name: String, input:String, onChange:(String)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){

        Text(text = name,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            fontSize = 16.sp
        )

        OutlinedTextField( value = input,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Red),
            onValueChange = { onChange(it)}
        )


    }
}

@Composable
fun InputPasswordBorder(name: String, input:String, onChange:(String)->Unit, visibility: Boolean, onVisibileChange:()->Unit){
    Column(modifier = Modifier.fillMaxWidth()){

        Text(text = name,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            fontSize = 16.sp
        )

        OutlinedTextField( value = input,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Red),
            onValueChange = { onChange(it)},
            visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (visibility){
                    painterResource(id = R.drawable.visibility)

                } else {
                    painterResource(id = R.drawable.visibility_off)
                }
                IconButton(onClick = {onVisibileChange()}) {
                    Icon(painter = image, null)
                }
            }
        )


    }
}