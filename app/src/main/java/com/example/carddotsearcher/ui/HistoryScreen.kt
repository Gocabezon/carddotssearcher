package com.example.carddotsearcher.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun HistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val searchHistory by viewModel.searchHistory.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(searchHistory) { card ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { // Make the card clickable
                        viewModel.searchSpecificCard(card)
                        navController.navigate("results")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = card.name)
                }
            }
        }
    }
}
