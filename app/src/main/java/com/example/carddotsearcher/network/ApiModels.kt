package com.example.carddotsearcher.network

import com.squareup.moshi.Json

// 1. Representa el objeto principal que devuelve la API: { "data": [...] }
data class ApiResponse(
    val data: List<ApiCard>
)

// 2. Representa cada carta dentro de la lista "data"
data class ApiCard(
    val name: String,
    val type: String,
    @Json(name = "card_images") val cardImages: List<CardImage>?
)

// 3. Representa el objeto dentro de la lista "card_images"
data class CardImage(
    @Json(name = "image_url") val imageUrl: String
)