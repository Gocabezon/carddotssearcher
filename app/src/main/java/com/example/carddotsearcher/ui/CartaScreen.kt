package com.example.carddotsearcher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage
import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.viewmodel.CartaViewModel

@Composable
fun CartaItem(
    carta: Carta,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                // CAMBIO: Accede directamente a la URL de la imagen del objeto Carta.
                model = carta.imageUrl,
                contentDescription = "Imagen de la carta ${carta.name}",
                modifier = Modifier.fillMaxWidth(),
                // Placeholder y error por si la carga falla o la URL está vacía.
                placeholder = painterResource(id = R.drawable.dragonblancoojosazules),
                error = painterResource(id = R.drawable.error)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // CAMBIO: Accede directamente al nombre del objeto Carta.
            Text(
                text = carta.name,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CartaScreen(
    modifier: Modifier = Modifier,
    // CAMBIO: La forma de obtener el ViewModel se simplifica.
    viewModel: CartaViewModel = viewModel()
) {
    // Observa el flujo de cartas del ViewModel.
    val cards by viewModel.posts.collectAsState()
    val searchQuery by viewModel.searchQuery

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Campo de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.handleSearch(it) },
            label = { Text("Buscar carta...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Comprueba si está cargando (si la lista está vacía después de una búsqueda)
        if (cards.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Malla para mostrar las cartas encontradas.
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cards) { carta ->
                    CartaItem(carta = carta)
                }
            }
        }
    }
}
