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
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun ResultsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val foundStores by viewModel.foundStores.observeAsState(emptyList())
    val selectedCard by viewModel.selectedCard.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val photoBitmap by viewModel.photoBitmap.observeAsState()
    var showImageDialog by remember { mutableStateOf(false) }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }


    var userLocation by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {
            // Comprobación de seguridad explícita
            if (hasLocationPermission(context)) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    userLocation = location
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

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
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )
                }
                selectedCard?.let {
                    Text(text = "Carta seleccionada: ${it.name}", modifier = Modifier.padding(bottom = 16.dp))
                }

                if (foundStores.isNotEmpty()) {
                    if (userLocation == null) {
                        CircularProgressIndicator(modifier = Modifier.size( 16.dp))
                        Text("Cargando ubicación...", modifier = Modifier.padding(top = 8.dp))
                    } else {
                        StoresList(stores = foundStores, userLocation = userLocation, onImageClick = { imageRes ->
                            selectedImageRes = imageRes
                            showImageDialog = true
                        })
                    }
                } else {
                    Text("No se encontraron tiendas para esta carta.")
                }
                if (showImageDialog && selectedImageRes != null) {
                    Dialog(onDismissRequest = { showImageDialog = false }) {
                        Card(
                            modifier = Modifier
                                .size(300.dp) // Tamaño del diálogo con la imagen grande
                        ) {
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
}

@Composable
fun StoresList(stores: List<Tienda>, userLocation: Location?, onImageClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(stores) { store ->
            val distance = userLocation?.let { calculateDistance(it.latitude, it.longitude, store.latitude, store.longitude) }

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
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { onImageClick(store.imageRes) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "${store.name} tiene la carta")
                        distance?.let {
                            Text(text = "Distancia: %.2f km".format(it), style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp))
                        }
                    }
                }
            }
        }
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0] / 1000 // Convertir a kilómetros
}
