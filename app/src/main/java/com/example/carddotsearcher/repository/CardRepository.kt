
package com.example.carddotsearcher.repository


import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.network.ApiCard // <-- Se añade la importación correcta
import com.example.carddotsearcher.network.ApiService

import com.example.carddotsearcher.network.ApiResponse
import com.example.carddotsearcher.network.RetrofitInstance
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types // <-- Se usa la importación correcta de Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody

class CardRepository {

    private val apiService: ApiService = RetrofitInstance.api

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // --- LÍNEA CORREGIDA ---
    // Ahora usa la clase 'Types' directamente de Moshi, no de 'privacysandbox'.
    private val cardListAdapter: JsonAdapter<List<ApiCard>> = moshi.adapter(
        Types.newParameterizedType(List::class.java, ApiCard::class.java)
    )

    private val singleCardAdapter: JsonAdapter<ApiCard> = moshi.adapter(ApiCard::class.java)

    private val allStores: List<Tienda> = listOf(
        Tienda(
            name = "Tienda Rebelde",
            cards = listOf(Carta(name = "Dark Magician", type = "", imageUrl = "")),
            cardStock = 10,
            imageRes = R.drawable.tienda1, // Asegúrate de que 'tienda1' existe en res/drawable
            latitude = -33.45694,
            longitude = -70.64827
        ),
        Tienda(
            name = "Metropolis Center",
            cards = listOf(Carta(name = "Blue-Eyes White Dragon", type = "", imageUrl = "")),
            cardStock = 5,
            imageRes = R.drawable.tienda2, // Asegúrate de que 'tienda2' existe en res/drawable
            latitude = -33.44889,
            longitude = -70.66926
        )
    )

    fun getAllStores(): List<Tienda> {
        return allStores
    }

    suspend fun getRandomCard(): Carta {
        try {
            // --- PUNTO CLAVE: Adaptador para la respuesta completa de la API ---
            val apiResponseAdapter: JsonAdapter<ApiResponse> = moshi.adapter(ApiResponse::class.java)

            val responseBody = apiService.getRandomCardAsJson()
            val jsonString = responseBody.string()

            // 1. Parsea el JSON completo usando el adaptador correcto (ApiResponse).
            val apiResponse = apiResponseAdapter.fromJson(jsonString)

            // 2. Extrae la lista de cartas de la respuesta.
            val cardList = apiResponse?.data

            // 3. Filtra la lista para quedarte solo con cartas que tengan imágenes.
            val cardsWithImages = cardList?.filter { !it.cardImages.isNullOrEmpty() }

            // 4. Si la lista filtrada no está vacía, elige una carta al azar.
            if (!cardsWithImages.isNullOrEmpty()) {
                val randomApiCard = cardsWithImages.first() // La API randomcard.php solo devuelve una

                // 5. Convierte el ApiCard a tu modelo de dominio Carta.
                return Carta(
                    name = randomApiCard.name,
                    type = randomApiCard.type,
                    imageUrl = randomApiCard.cardImages!!.first().imageUrl
                )
            } else {
                // Si la carta aleatoria no tenía imagen, reintenta.
                return getRandomCard()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Devuelve la carta de error si todo falla.
            return Carta(
                name = "Error de red",
                type = "No se pudo obtener una carta",
                imageUrl = ""
            )
        }
    }

    suspend fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { store -> store.cards.any { it.name == card.name } }
    }
}
