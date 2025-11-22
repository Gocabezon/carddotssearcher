package com.example.carddotsearcher.model

import androidx.annotation.DrawableRes

data class Tienda(
    val name: String,
    val inventory: List<InventoryItem>,
    @DrawableRes val imageRes: Int,
    val latitude: Double,
    val longitude: Double
)
