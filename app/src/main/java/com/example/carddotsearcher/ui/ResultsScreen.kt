package com.example.carddotsearcher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun ResultsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val foundStores by viewModel.foundStores.observeAsState(emptyList())
    val selectedCard by viewModel.selectedCard.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
    LazyColumn(modifier = modifier.padding(top = 16.dp)) {
        items(stores) { store ->
            Text(text = "${store.name} tiene la carta", modifier = Modifier.padding(8.dp))
        }
    }
}
