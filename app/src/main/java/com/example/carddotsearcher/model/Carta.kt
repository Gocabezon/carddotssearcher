// En model/Carta.kt
package com.example.carddotsearcher.model

// Ya no necesitas @DrawableRes
data class Carta(
    val name: String,
    val type: String,
    val imageUrl: String // Cambiado de imageRes: Int a imageUrl: String
)
