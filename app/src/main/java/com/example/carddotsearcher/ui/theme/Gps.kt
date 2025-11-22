// En ui/theme/Gps.kt
package com.example.carddotsearcher.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.viewmodel.MainViewModel
import com.google.android.gms.location.LocationServices

// --- CLASE DE DATOS AUXILIAR ---
data class FindStoreResult(
    val userLocation: Location,
    val nearestStore: Tienda?,
    val distance: Float?
)

// --- Composable Principal ---
@Composable
fun GPS(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    // Estados para gestionar la UI
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var nearestStoreResult by remember { mutableStateOf<FindStoreResult?>(null) }
    var selectedStore by remember { mutableStateOf<Tienda?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Toca 'Buscar Tienda Cercana' para empezar.") }
    var expanded by remember { mutableStateOf(false) }

    // --- CAMBIO 1: Obtiene TODAS las tiendas, sin filtrar por un stock que ya no existe. ---
    val allStores = remember { viewModel.getAllStores() }

    // Launcher para permisos (sin cambios, ya es correcto)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            findNearestStore(context, allStores,
                onLoading = {
                    isLoading = true
                    statusText = "Permiso concedido. Obteniendo ubicación..."
                },
                onResult = { result ->
                    isLoading = false
                    userLocation = result.userLocation
                    nearestStoreResult = result
                    selectedStore = result.nearestStore
                    statusText = if (result.nearestStore != null) {
                        "¡Tienda más cercana encontrada!"
                    } else {
                        "Ubicación obtenida, pero no hay tiendas en la lista."
                    }
                },
                onError = { error ->
                    isLoading = false
                    statusText = error
                }
            )
        } else {
            statusText = "Permiso de ubicación denegado."
            Toast.makeText(context, statusText, Toast.LENGTH_LONG).show()
        }
    }

    // Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) }
            ) {
                Text("Buscar Tienda Cercana")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Muestra un selector de tiendas si se ha encontrado al menos una
        nearestStoreResult?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selecciona una tienda:")

            Box {
                Button(onClick = { expanded = true }) {
                    Text(selectedStore?.name ?: "Seleccionar tienda")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allStores.forEach { store ->
                        DropdownMenuItem(
                            // --- CAMBIO 2: Mostramos el número de cartas en inventario ---
                            text = { Text("${store.name} - ${store.inventory.size} tipos de cartas") },
                            onClick = {
                                selectedStore = store
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra los detalles de la tienda seleccionada
        selectedStore?.let { store ->
            val distanceKm = userLocation?.let {
                val storeLocation = Location("").apply {
                    latitude = store.latitude
                    longitude = store.longitude
                }
                it.distanceTo(storeLocation) / 1000f
            }

            Text(
                text = "Tienda Seleccionada:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("Nombre: ${store.name}")
            distanceKm?.let { Text("Distancia: %.2f km".format(it)) }

            // --- CAMBIO 3: Ya no mostramos un 'stock' general. ---
            // En su lugar, podrías mostrar el número total de cartas disponibles.
            val totalStock = store.inventory.sumOf { it.stock }
            Text("Cartas totales en stock: $totalStock")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showDirections(context, store) }) {
                Text("Ir con Google Maps")
            }
        }
    }
}

// --- FUNCIÓN DE LÓGICA (Separada de la UI) ---
@SuppressLint("MissingPermission")
private fun findNearestStore(
    context: Context,
    stores: List<Tienda>,
    onLoading: () -> Unit,
    onResult: (FindStoreResult) -> Unit,
    onError: (String) -> Unit
) {
    onLoading()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location == null) {
                onError("No se pudo obtener la ubicación. Activa el GPS y vuelve a intentarlo.")
                return@addOnSuccessListener
            }

            if (stores.isEmpty()) {
                onResult(FindStoreResult(location, null, null))
                return@addOnSuccessListener
            }

            // Encuentra la tienda más cercana (sin cambios, ya era correcto)
            val nearestStoreWithDistance = stores.map { store ->
                val storeLocation = Location("").apply {
                    latitude = store.latitude
                    longitude = store.longitude
                }
                val distance = location.distanceTo(storeLocation)
                Pair(store, distance)
            }.minByOrNull { it.second }

            onResult(FindStoreResult(location, nearestStoreWithDistance?.first, nearestStoreWithDistance?.second))
        }
        .addOnFailureListener {
            onError("Falló la obtención de la ubicación: ${it.message}")
        }
}

// --- FUNCIÓN AUXILIAR (sin cambios) ---
private fun showDirections(context: Context, store: Tienda) {
    val uri = "google.navigation:q=${store.latitude},${store.longitude}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo iniciar Google Maps.", Toast.LENGTH_LONG).show()
    }
}
