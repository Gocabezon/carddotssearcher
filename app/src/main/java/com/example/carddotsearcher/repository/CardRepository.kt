// En repository/CardRepository.kt
package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.InventoryItem
import com.example.carddotsearcher.model.Tienda

import com.example.carddotsearcher.network.ApiResponse
import com.example.carddotsearcher.network.ApiService
import com.example.carddotsearcher.network.RetrofitInstance
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import kotlin.random.Random

class CardRepository {

    private val apiService: ApiService = RetrofitInstance.api
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val apiResponseAdapter: JsonAdapter<ApiResponse> = moshi.adapter(ApiResponse::class.java)

    // --- PUNTO CLAVE 1: La lista de tiendas ahora se genera dinámicamente con datos locales ---
    private val allStores: List<Tienda> by lazy {
        // Usamos 'by lazy' para que esta lógica de creación se ejecute solo una vez.
        generateMockStores()
    }

    /**
     * Genera una lista de tiendas con inventarios aleatorios a partir de nuestros datos mock.
     */
    private fun generateMockStores(): List<Tienda> {
        val allMockCards = MockCardDataSource.mockCards

        // Tienda 1: Tienda Rebelde
        val rebeldeInventory = mutableListOf<InventoryItem>()
        // Asignamos 50 cartas aleatorias a esta tienda
        repeat(50) {
            val randomCard = allMockCards.random()
            rebeldeInventory.add(
                InventoryItem(
                    card = randomCard,
                    price = Random.nextDouble(1.0, 50.0), // Precio aleatorio entre 1 y 50
                    stock = Random.nextInt(1, 10)         // Stock aleatorio entre 1 y 9
                )
            )
        }

        // Tienda 2: Metropolis Center
        val metropolisInventory = mutableListOf<InventoryItem>()
        // Asignamos otras 50 cartas aleatorias a esta tienda
        repeat(50) {
            val randomCard = allMockCards.random()
            metropolisInventory.add(
                InventoryItem(
                    card = randomCard,
                    price = Random.nextDouble(0.5, 60.0), // Rango de precios diferente
                    stock = Random.nextInt(1, 5)          // Stock más bajo
                )
            )
        }

        return listOf(
            Tienda(
                name = "Tienda Rebelde",
                inventory = rebeldeInventory.distinctBy { it.card.name }, // Evita duplicados de la misma carta
                imageRes = R.drawable.tienda1,
                latitude = -33.45694,
                longitude = -70.64827
            ),
            Tienda(
                name = "Metropolis Center",
                inventory = metropolisInventory.distinctBy { it.card.name }, // Evita duplicados
                imageRes = R.drawable.tienda2,
                latitude = -33.44889,
                longitude = -70.66926
            )
        )
    }

    /**
     * FUNCIÓN PÚBLICA para acceder a las tiendas.
     */
    fun getAllStores(): List<Tienda> {
        return allStores
    }

    /**
     * Usa el endpoint de la API para obtener UNA carta completamente aleatoria.
     * Esta es la única parte que usa la red.
     */
    suspend fun getRandomCard(): Carta {
        try {
            val responseBody = apiService.getRandomCardAsJson()
            val jsonString = responseBody.string()

            val apiResponse = apiResponseAdapter.fromJson(jsonString)
            val cardList = apiResponse?.data
            val cardsWithImages = cardList?.filter { !it.cardImages.isNullOrEmpty() }

            if (!cardsWithImages.isNullOrEmpty()) {
                val randomApiCard = cardsWithImages.first()
                // La API nos da la URL de la imagen, la usamos para el objeto Carta que devolveremos
                return Carta(
                    name = randomApiCard.name,
                    type = randomApiCard.type,
                    imageUrl = randomApiCard.cardImages!!.first().imageUrl
                )
            } else {
                return getRandomCard() // Reintento si la carta no tiene imagen
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Carta(name = "Error de red", type = "No se pudo obtener una carta", imageUrl = "")
        }
    }

    /**
     * Devuelve una lista de Tiendas que tienen esa carta en su stock.
     * Esta lógica ahora funciona con el inventario generado localmente.
     */
    fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { store ->
            store.inventory.any { inventoryItem ->
                inventoryItem.card.name == card.name
            }
        }
    }
}
