package com.example.carddotsearcher.ui

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
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    val searchHistory by viewModel.searchHistory.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(searchHistory) { card ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = card.name)
                }
            }
        }
    }
}
