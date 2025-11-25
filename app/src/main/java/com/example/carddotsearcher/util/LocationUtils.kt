package com.example.carddotsearcher.util

import android.location.Location

// La función ahora es pública y está en su propio archivo
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0] / 1000 // Devuelve la distancia en kilómetros
}
    