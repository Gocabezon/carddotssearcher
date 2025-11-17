package com.example.carddotsearcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carddotsearcher.R
import com.example.carddotsearcher.repository.UsuarioRepository

@Composable
fun RegistroScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val repository = remember { UsuarioRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Usuario", modifier = Modifier.padding(bottom = 24.dp))

        Image(
            painter = painterResource(id = R.drawable.carddotsearcher),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 24.dp)
        )
        Text(text = "CardDot Searcher", modifier = Modifier.padding(bottom = 24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; errorMessage = "" },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = "" },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {

                if (username.isBlank() || password.isBlank()) {
                    // Usuario o contraseña vacíos → enviar a registro
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = false }
                    }
                    return@Button
                }

                // Intentar registrar
                val success = repository.registerUser(username, password)

                if (success) {
                    // Registro correcto → ir a cámara
                    navController.navigate("camera") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    // Usuario ya existe
                    errorMessage = "El nombre de usuario ya existe"
                }

            }) {
                Text("Registrarse")
            }
        }
    }
}