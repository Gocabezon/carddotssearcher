// En repository/CardRepository.kt
package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.InventoryItem
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.network.ApiService
import com.example.carddotsearcher.network.RetrofitInstance
import kotlin.random.Random

class CardRepository {

    private val apiService: ApiService = RetrofitInstance.api

    val allStores: List<Tienda> by lazy {
        generateMockStores()
    }

    private fun generateMockStores(): List<Tienda> {
        val allMockCards = MockCardDataSource.mockCards

        val rebeldeInventory = mutableListOf<InventoryItem>()
        repeat(50) {
            val randomCard = allMockCards.random()
            rebeldeInventory.add(
                InventoryItem(
                    cardName = randomCard.name,
                    price = Random.nextDouble(1.0, 50.0),
                    stock = Random.nextInt(1, 10)
                )
            )
        }

        val metropolisInventory = mutableListOf<InventoryItem>()
        repeat(60) {
            val randomCard = allMockCards.random()
            metropolisInventory.add(
                InventoryItem(
                    cardName = randomCard.name,
                    price = Random.nextDouble(0.5, 60.0),
                    stock = Random.nextInt(1, 5)
                )
            )
        }

        return listOf(
            Tienda(
                name = "Tienda Rebelde",
                inventory = rebeldeInventory.distinctBy { it.cardName },
                imageRes = R.drawable.tienda1,
                latitude = -33.45694,
                longitude = -70.64827
            ),
            Tienda(
                name = "Metropolis Center",
                inventory = metropolisInventory.distinctBy { it.cardName },
                imageRes = R.drawable.tienda2,
                latitude = -33.44889,
                longitude = -70.66926
            )
        )
    }

    suspend fun searchCardByName(cardName: String): Carta? {
        return try {
            val response = apiService.searchCardByName(cardName)
            val cardData = response.data.firstOrNull()

            cardData?.let {
                Carta(
                    name = it.name,
                    type = it.type,
                    imageUrl = it.cardImages?.firstOrNull()?.imageUrl ?: ""
                )
            }
        } catch (e: Exception) {
            println("Error al buscar la carta '$cardName': ${e.message}")
            null
        }
    }

    fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { store ->
            store.inventory.any { inventoryItem ->
                inventoryItem.cardName.equals(card.name, ignoreCase = true)
            }
        }
    }
}
