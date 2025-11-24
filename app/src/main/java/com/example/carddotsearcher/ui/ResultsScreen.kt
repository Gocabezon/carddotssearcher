

package com.example.carddotsearcher.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel
import com.google.android.gms.location.LocationServices

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0] / 1000 // Devuelve la distancia en kilómetros
}
@Composable
fun StoresList(
    stores: List<Tienda>,
    userLocation: Location?,
    selectedCard: Carta?,
    navController: NavController,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedCard == null) return

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        items(stores) { store ->
            val inventoryItem = store.inventory.find {
                it.cardName.equals(selectedCard.name, ignoreCase = true) }
            val distance = userLocation?.let {
                calculateDistance(it.latitude, it.longitude, store.latitude, store.longitude)
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { navController.navigate("gps") },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = store.imageRes),
                        contentDescription = "Imagen de ${store.name}",
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { onImageClick(store.imageRes) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = store.name, style = MaterialTheme.typography.titleMedium)
                        inventoryItem?.let {
                            Text("Precio: $${"%.2f".format(it.price)}", style = MaterialTheme.typography.bodyMedium)
                            Text("Stock: ${it.stock}", style = MaterialTheme.typography.bodySmall)
                        }
                        distance?.let {
                            Text("Distancia: %.2f km".format(it), style = TextStyle(fontSize = 12.sp))
                        }
                    }
                }
            }
        }
    }
}

// ... (funciones auxiliares hasLocationPermission y calculateDistance se mantienen igual)

@SuppressLint("MissingPermission")
@Composable
fun ResultsScreen(
    navController: NavController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val foundStores by viewModel.foundStores.observeAsState(emptyList())
    val selectedCard by viewModel.selectedCard.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    var showImageDialog by remember { mutableStateOf(false) }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }

    var userLocation by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                userLocation = location
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                selectedCard?.let { card ->
                    AsyncImage(
                        model = card.imageUrl,
                        contentDescription = "Imagen de la carta ${card.name}",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth(0.6f)
                            .padding(bottom = 16.dp),
                        placeholder = painterResource(id = R.drawable.dragonblancoojosazules),
                        error = painterResource(id = R.drawable.error)
                    )
                    Text(text = "Carta encontrada: ${card.name}", modifier = Modifier.padding(bottom = 16.dp))
                }

                if (foundStores.isNotEmpty()) {
                    if (userLocation == null) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cargando ubicación...")
                        }
                    } else {
                        StoresList(
                            stores = foundStores,
                            userLocation = userLocation,
                            selectedCard = selectedCard,
                            navController = navController,
                            onImageClick = { imageRes ->
                                selectedImageRes = imageRes
                                showImageDialog = true
                            }
                        )
                    }
                } else {
                    if (selectedCard != null) {
                        Text("No se encontraron tiendas para esta carta.")
                    } else {
                        Text("No se ha encontrado ninguna carta.") // Mensaje si se llega aquí sin carta
                    }
                }
            }
            // --- FIN DEL CÓDIGO RESTAURADO ---

            // El diálogo para ampliar la imagen se mantiene igual
            if (showImageDialog && selectedImageRes != null) {
                Dialog(onDismissRequest = { showImageDialog = false }) {
                    Card(modifier = Modifier.size(300.dp)) {
                        Image(
                            painter = painterResource(id = selectedImageRes!!),
                            contentDescription = "Imagen de la tienda ampliada",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
