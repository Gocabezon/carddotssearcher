package com.example.carddotsearcher.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carddotsearcher.viewmodel.AuthViewModel

@Composable
fun LogoutButtonRow(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End // Alinea el botón a la derecha
    ) {
        Button(onClick = { authViewModel.logout() }) {
            Text("Cerrar Sesión")
        }
    }
}
