package com.example.carddotsearcher.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Usuario
import com.example.carddotsearcher.viewmodel.AuthViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    // 1. OBTENEMOS LA INSTANCIA DEL VIEWMODEL CORRECTAMENTE
    authViewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Observamos los estados que vienen del ViewModel
    val authState by authViewModel.authState.collectAsState()
    val loggedInUser by authViewModel.loggedInUser.collectAsState()

    // --- EFECTOS SECUNDARIOS: Reaccionan a los cambios de estado ---

    // Efecto para navegar cuando el registro sea exitoso
    LaunchedEffect(loggedInUser) {
        if (loggedInUser != null) {
            // Si hay un usuario logueado (el registro fue exitoso e inició sesión),
            // navegamos a la cámara y limpiamos la pila de navegación.
            navController.navigate("camera") {
                // Limpia todas las pantallas anteriores hasta el inicio para no poder volver atrás
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // Efecto para mostrar mensajes de error
    LaunchedEffect(authState) {
        if (authState == AuthViewModel.AuthState.Error) {
            Toast.makeText(context, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show()
            authViewModel.resetAuthState() // Reseteamos el estado para poder reintentar
        }
    }

    // --- INTERFAZ DE USUARIO ---

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

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Si el estado es "Loading", mostramos una rueda de carga
        if (authState == AuthViewModel.AuthState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    // 2. DELEGAMOS LA LÓGICA DE REGISTRO AL VIEWMODEL
                    authViewModel.register(Usuario(username, password))
                } else {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Botón para ir a la pantalla de login si ya tiene cuenta
            navController.navigate("login")
        }) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}
