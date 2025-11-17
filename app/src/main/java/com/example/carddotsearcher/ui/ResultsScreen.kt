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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel
import com.google.android.gms.location.LocationServices
import coil.compose.AsyncImage
import com.example.carddotsearcher.R // <-- Esta es la correcta


// --- FUNCIONES AUXILIARES ---
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
    return results[0] / 1000 // Devuelve la distancia en kilómetros
}

// ---------------------------

@Composable
fun StoresList(
    stores: List<Tienda>,
    userLocation: Location?,
    navController: NavController,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(stores) { store ->
            val distance = userLocation?.let {
                calculateDistance(
                    it.latitude,
                    it.longitude,
                    store.latitude,
                    store.longitude
                )
            }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            // Navega a la pantalla GPS al hacer clic en el item
                            .clickable {
                                navController.navigate("gps")
                            }
                    ) {
                        Text(text = "${store.name} tiene la carta")

                        distance?.let {
                            Text(
                                text = "Distancia: %.2f km".format(it),
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

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
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {

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

                selectedCard?.let { card ->
                    AsyncImage(
                        model = card.imageUrl, // Le pasas la URL directamente
                        contentDescription = "Imagen de la carta ${card.name}",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 16.dp),
                        placeholder = painterResource(id = R.drawable.dragonblancoojosazules), // Muestra esto mientras carga
                        error = painterResource(id = R.drawable.dragonblancoojosazules) // Muestra esto si hay un error
                    )
                    Text(text = "Carta encontrada: ${card.name}", modifier = Modifier.padding(bottom = 16.dp))
                }

                if (foundStores.isNotEmpty()) {
                    if (userLocation == null) {
                        CircularProgressIndicator(modifier = Modifier.size( 16.dp))
                        Text("Cargando ubicación...", modifier = Modifier.padding(top = 8.dp))
                    } else {
                        StoresList(
                            stores = foundStores,
                            userLocation = userLocation,
                            navController = navController, // Pasa navController
                            onImageClick = { imageRes ->
                                selectedImageRes = imageRes
                                showImageDialog = true
                            }
                        )
                    }
                } else {
                    Text("No se encontraron tiendas para esta carta.")
                }

                if (showImageDialog && selectedImageRes != null) {
                    Dialog(onDismissRequest = { showImageDialog = false }) {
                        Card(
                            modifier = Modifier
                                .size(300.dp)
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
