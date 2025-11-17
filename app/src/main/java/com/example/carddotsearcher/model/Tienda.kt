package com.example.carddotsearcher.model

import androidx.annotation.DrawableRes

data class Tienda(
    val name: String,
    val cards: List<Carta>,
    val cardStock: Int,
    @DrawableRes val imageRes: Int,
    val latitude: Double,
    val longitude: Double
)
