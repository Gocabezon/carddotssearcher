// En model/InventoryItem.kt
package com.example.carddotsearcher.model

data class InventoryItem(
    val cardName: String,
    val price: Double, // Usamos Double para representar el precio (ej: 10.99)
    val stock: Int     // El stock de esta carta específica en la tienda
)
