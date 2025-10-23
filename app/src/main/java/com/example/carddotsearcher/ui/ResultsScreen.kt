package com.example.carddotsearcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun ResultsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val foundStores by viewModel.foundStores.observeAsState(emptyList())
    val selectedCard by viewModel.selectedCard.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val photoBitmap by viewModel.photoBitmap.observeAsState()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                photoBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Foto de la carta",
                        modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
                    )
                }
                selectedCard?.let {
                    Text(text = "Carta seleccionada: ${it.name}", modifier = Modifier.padding(bottom = 16.dp))
                }

                if (foundStores.isNotEmpty()) {
                    StoresList(stores = foundStores)
                } else {
                    Text("No se encontraron tiendas para esta carta.")
                }
            }
        }
    }
}

@Composable
fun StoresList(stores: List<Tienda>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(stores) { store ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = store.imageRes),
                        contentDescription = "Imagen de ${store.name}",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "${store.name} tiene la carta")
                }
            }
        }
    }
}
