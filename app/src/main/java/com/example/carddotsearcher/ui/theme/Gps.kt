package com.example.carddotsearcher.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.repository.CardRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority




private fun showDirections(context: Context, store: Tienda) {
    // URI para iniciar la navegación a las coordenadas de la tienda.
    val uri = "google.navigation:q=${store.latitude},${store.longitude}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "No se pudo iniciar Google Maps. Verifica que la app esté instalada.",
            Toast.LENGTH_LONG
        ).show()
    }
}

@SuppressLint("MissingPermission")
fun obtenerUbicacion(context: Context, onLocation: (Location?) -> Unit, onFail: (String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onFail("Error: Permisos no concedidos. No se puede obtener la ubicación.")
        return
    }


    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                onLocation(location)
                fusedLocationClient.removeLocationUpdates(this)
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                if (!p0.isLocationAvailable) {
                    onFail("GPS no disponible. Asegúrate de que está activo.")
                    fusedLocationClient.removeLocationUpdates(this)
                }
                super.onLocationAvailability(p0)
            }
        },
        Looper.getMainLooper()
    ).addOnFailureListener {
        onFail("Error desconocido al solicitar ubicación: ${it.message}")
    }
}

private fun findBestStore(userLocation: Location): Pair<Tienda, Float>? {
    val storesWithStock = CardRepository().obtenerTodasLasTiendas().filter { it.cardStock > 0 }

    if (storesWithStock.isEmpty()) return null

    var nearestStore: Tienda? = null
    var shortestDistance = Float.MAX_VALUE

    for (store in storesWithStock) {
        val storeLocation = Location("store_provider").apply {
            latitude = store.latitude
            longitude = store.longitude
        }

        val distanceInMeters = userLocation.distanceTo(storeLocation)

        if (distanceInMeters < shortestDistance) {
            shortestDistance = distanceInMeters
            nearestStore = store
        }
    }
    return nearestStore?.let { Pair(it, shortestDistance) }
}




@Composable
fun GPS(navController: NavController) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var nearestStoreResult by remember { mutableStateOf<Pair<Tienda, Float>?>(null) }
    var selectedStore by remember { mutableStateOf<Tienda?>(null) }
    var allStoresWithStock by remember { mutableStateOf<List<Tienda>>(emptyList()) }

    var statusText by remember { mutableStateOf("Toca el botón para buscar tu ubicación y la tienda.") }
    var isFinding by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            statusText = "Permiso concedido. Obteniendo ubicación..."
            isFinding = true
            obtenerUbicacion(
                context,
                onLocation = { location ->
                    isFinding = false
                    if (location != null) {
                        currentLocation = location
                        statusText = "Ubicación obtenida. Buscando tienda más cercana..."

                        val tiendasConStock = CardRepository().obtenerTodasLasTiendas().filter { it.cardStock > 0 }
                        allStoresWithStock = tiendasConStock

                        val result = findBestStore(location)
                        nearestStoreResult = result
                        selectedStore = result?.first

                        statusText = if (result == null) {
                            "Ninguna tienda en nuestra lista tiene stock."
                        } else {
                            "¡Tienda encontrada! Puedes cambiarla si lo deseas."
                        }
                    } else {
                        statusText = "No se pudo obtener la ubicación (Location es null)."
                    }
                },
                onFail = { errorMsg ->
                    isFinding = false
                    statusText = errorMsg
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            )
        } else {
            statusText = "Permisos de ubicación denegados."
            Toast.makeText(
                context,
                "Se necesitan permisos de ubicación para buscar tiendas.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Estado: $statusText", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                currentLocation = null
                nearestStoreResult = null
                selectedStore = null
                allStoresWithStock = emptyList()
                statusText = "Iniciando búsqueda..."

                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    statusText = "Obteniendo ubicación..."
                    isFinding = true
                    obtenerUbicacion(
                        context,
                        onLocation = { location ->
                            isFinding = false
                            if (location != null) {
                                currentLocation = location
                                statusText = "Ubicación obtenida. Buscando tienda más cercana..."

                                val tiendasConStock = CardRepository().obtenerTodasLasTiendas()
                                    .filter { it.cardStock > 0 }
                                allStoresWithStock = tiendasConStock

                                val result = findBestStore(location)
                                nearestStoreResult = result
                                selectedStore = result?.first

                                statusText = if (result == null) {
                                    "Ninguna tienda en nuestra lista tiene stock."
                                } else {
                                    "¡Tienda encontrada! Puedes cambiarla si lo deseas."
                                }
                            } else {
                                statusText = "No se pudo obtener la ubicación (Location es null)."
                            }
                        },
                        onFail = { errorMsg ->
                            isFinding = false
                            statusText = errorMsg
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    )
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            enabled = !isFinding
        ) {
            Text(if (isFinding) "Buscando..." else "Buscar Tienda con Stock")
        }

        Spacer(modifier = Modifier.height(32.dp))

        currentLocation?.let {
            Text("Tu ubicación: Latitud ${"%.5f".format(it.latitude)}, Longitud ${"%.5f".format(it.longitude)}")
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (allStoresWithStock.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selecciona otra tienda si no quieres la más cercana:")

            Box {
                Button(onClick = { expanded = true }) {
                    Text(selectedStore?.name ?: "Selecciona una tienda")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allStoresWithStock.forEach { store ->
                        DropdownMenuItem(
                            text = { Text("${store.name} - Stock: ${store.cardStock}") },
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

        selectedStore?.let { store ->
            val distanceMeters = currentLocation?.let {
                val storeLocation = Location("").apply {
                    latitude = store.latitude
                    longitude = store.longitude
                }
                it.distanceTo(storeLocation)
            } ?: 0f

            val distanceKm = distanceMeters / 1000f

            Text(
                text = "Tienda seleccionada:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("Nombre: ${store.name}")
            Text("Distancia: ${"%.2f".format(distanceKm)} km")
            Text("Stock: ${store.cardStock}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                showDirections(context, store)
            }) {
                Text("Mostrar Ruta en Google Maps")
            }
        }
    }
}