// En network/ApiService.kt
package com.example.carddotsearcher.network

import okhttp3.ResponseBody

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Endpoint para obtener todas las cartas o una específica por nombre
    // https://db.ygoprodeck.com/api/v7/cardinfo.php?name=Dark Magician
    @GET("api/v7/cardinfo.php")
    suspend fun searchCardByName(@Query("name") cardName: String): ApiResponse

    @GET("api/v7/randomcard.php")
    suspend fun getRandomCardAsJson(): ResponseBody // Devuelve un elemento JSON genérico
}
