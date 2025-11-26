package com.example.carddotsearcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carddotsearcher.viewmodel.MainViewModel

@Composable
fun CameraContainer(navController: NavController, viewModel: MainViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {

        CamaraFotos(viewModel = viewModel, onPhotoTaken = {
            viewModel.searchRandomCard()
            navController.navigate("results")
        })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("history") }) {
            Text("Ver Historial")
        }


    }
}