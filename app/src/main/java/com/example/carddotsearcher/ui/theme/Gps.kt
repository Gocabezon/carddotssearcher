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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

// --- LÓGICA DE LA PANTALLA GPS ---
@Composable
fun GPS(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    // 1. ESTADOS PARA GESTIONAR LA UI
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var nearestStore by remember { mutableStateOf<Tienda?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Toca 'Buscar Tienda Cercana' para empezar.") }

    // 2. OBTENER LAS TIENDAS DESDE EL VIEWMODEL (Forma correcta)
    // Esto es más eficiente, se obtiene la lista una sola vez.
    val storesWithStock = remember { viewModel.getAllStores().filter { it.cardStock > 0 } }

    // 3. LAUNCHER PARA PERMISOS (Igual que en ResultsScreen, simple y efectivo)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            // Permiso concedido, ahora obtenemos la ubicación.
            findNearestStore(context, storesWithStock,
                onLoading = {
                    isLoading = true
                    statusText = "Permiso concedido. Obteniendo ubicación..."
                },
                onResult = { result ->
                    isLoading = false
                    userLocation = result.userLocation
                    nearestStore = result.nearestStore
                    statusText = if (result.nearestStore != null) {
                        "¡Tienda más cercana encontrada!"
                    } else {
                        "Ubicación obtenida, pero no se encontraron tiendas con stock."
                    }
                },
                onError = { error ->
                    isLoading = false
                    statusText = error
                }
            )
        } else {
            // Permiso denegado.
            statusText = "Permiso de ubicación denegado. No se puede buscar la tienda."
            Toast.makeText(context, statusText, Toast.LENGTH_LONG).show()
        }
    }

    // 4. DISEÑO DE LA PANTALLA (Column con la información)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Muestra el estado actual
        Text(
            text = statusText,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Muestra un indicador de carga si está buscando
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Muestra el botón para iniciar la búsqueda
        Button(
            onClick = { locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) },
            enabled = !isLoading
        ) {
            Text("Buscar Tienda Cercana")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Muestra la información de la tienda encontrada
        nearestStore?.let { store ->
            val distanceKm = userLocation?.let {
                val storeLocation = Location("").apply {
                    latitude = store.latitude
                    longitude = store.longitude
                }
                it.distanceTo(storeLocation) / 1000f
            }

            Text("Tienda Encontrada:", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Nombre: ${store.name}")
            distanceKm?.let { Text("Distancia: %.2f km".format(it)) }
            Text("Stock: ${store.cardStock}")
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showDirections(context, store) }) {
                Text("Ir con Google Maps")
            }
        }
    }
}

// --- CLASE DE DATOS AUXILIAR PARA UN RESULTADO LIMPIO ---
data class FindStoreResult(
    val userLocation: Location,
    val nearestStore: Tienda?,
    val distance: Float?
)

// --- FUNCIÓN DE LÓGICA PRINCIPAL (Separada de la UI) ---
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
                onError("No se pudo obtener la última ubicación conocida. Activa el GPS y vuelve a intentarlo.")
                return@addOnSuccessListener
            }

            if (stores.isEmpty()) {
                onResult(FindStoreResult(location, null, null))
                return@addOnSuccessListener
            }

            // Encuentra la tienda más cercana
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

// Función auxiliar para abrir Google Maps (sin cambios)
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
