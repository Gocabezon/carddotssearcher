package com.example.carddotsearcher.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carddotsearcher.R // <-- Necesaria para el placeholder
import com.example.carddotsearcher.viewmodel.CartaViewModel
import coil.compose.AsyncImage

@Composable
fun CartaScreen(navController: NavController, viewModel: CartaViewModel = viewModel()) {
    // Obtener la lista de cartas a mostrar (List<Carta>)
    // Aunque el StateFlow se llame 'posts', ahora contiene objetos Carta.
    val cartas = viewModel.posts.collectAsState().value

    // Obtener el estado del texto de búsqueda
    val currentSearchQuery = viewModel.searchQuery.value

    Column(modifier = Modifier.padding(16.dp)) {

        // 1. Campo de Búsqueda
        OutlinedTextField(
            value = currentSearchQuery,
            onValueChange = { newQuery ->
                viewModel.handleSearch(newQuery)
            },
            label = { Text("Buscar Carta...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Título Condicional (Ahora indicando 4 cartas)
        Text(
            text = if (currentSearchQuery.isBlank()) "4 Cartas Aleatorias" else "Resultados de la Búsqueda",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Lista de Resultados
        if (cartas.isEmpty()) {
            Text("No se encontraron cartas.", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Usamos LazyColumn para manejar la lista de manera eficiente si fuera muy larga
            LazyColumn {
                items(cartas) { carta -> // Iterar sobre cada objeto Carta
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Carga de la Imagen (usando el recurso drawable)
                            AsyncImage(
                                model = carta.imageUrl, // Usa la URL de la carta
                                contentDescription = "Imagen de la carta ${carta.name}",
                                placeholder = painterResource(id = R.drawable.dragonblancoojosazules), // Muestra esto mientras carga
                                error = painterResource(id = R.drawable.error), // Muestra esto si falla la carga
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Nombre de la carta
                            Text(
                                text = carta.name, // Usar el campo nombre
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}