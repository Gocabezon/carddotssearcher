package com.example.carddotsearcher.model

import androidx.annotation.DrawableRes

data class Tienda(val name: String, val cards: List<Carta>, @DrawableRes val imageRes: Int)
