// En ui/CameraContainer.kt

package com.example.carddotsearcher.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun CameraContainer(navController: NavController, viewModel: MainViewModel) {
    // --- NUEVO ESTADO PARA EL CAMPO DE TEXTO ---
    var searchFieldValue by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- SECCIÓN 1: BÚSQUEDA POR TEXTO ---
        Text(
            text = "Buscar carta por nombre",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = searchFieldValue,
            onValueChange = { searchFieldValue = it },
            label = { Text("Nombre de la carta...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Llama a la función de búsqueda del ViewModel
                viewModel.searchCardByName(searchFieldValue)
                // Navega a la pantalla de resultados
                navController.navigate("results")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar por Nombre")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN 2: BÚSQUEDA POR CÁMARA (la original) ---
        Text(
            text = "o buscar con la cámara",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // El Composable de la cámara, que ahora no ocupa toda la pantalla.
        // Lo envolvemos para que no sea tan grande.
        Box(modifier = Modifier.weight(1f)) {
            CamaraFotos(viewModel = viewModel, onPhotoTaken = {
                // La lógica de la cámara no cambia: busca una carta aleatoria
                viewModel.searchRandomCard()
                navController.navigate("results")
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // El botón de historial se mantiene al final
        Button(onClick = { navController.navigate("history") }) {
            Text("Ver Historial")
        }
    }
}
