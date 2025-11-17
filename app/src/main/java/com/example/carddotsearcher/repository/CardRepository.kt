//1. Corregir el nombre del paquete
package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R // Importación de recursos para las tiendas
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.network.ApiService
import com.example.carddotsearcher.network.RetrofitInstance

class CardRepository {

    companion object {
        fun obtenerTodasLasTiendas(): List<Tienda> {
            // Your existing logic to return the list of stores
            // For example:
            return listOf(
                // ... your Tienda objects
            )
        }
    }
    private val apiService: ApiService = RetrofitInstance.api

    // La lista de tiendas se mantiene local, ya que no viene de la API.
    private val localStores = listOf(
        Tienda(
            name = "Tienda Rebelde",
            cards = listOf(Carta("Dark Magician", "", "")),
            cardStock = 10, // Proporciona un valor para el stock
            imageRes = R.drawable.dragonblancoojosazules, // La imagen va aquí
            latitude = -33.45694,
            longitude = -70.64827
        ),
        Tienda(
            name = "Metropolis Center",
            cards = listOf(Carta("Blue-Eyes White Dragon", "", "")),
            cardStock = 5, // Proporciona un valor para el stock
            imageRes = R.drawable.dragonblancoojosazules, // La imagen va aquí
            latitude = -33.44889,
            longitude = -70.66926
        )
    )

    /**
     * Usa el endpoint de la API para obtener una carta completamente aleatoria.
     * Esta es la única versión de getRandomCard que necesitas.
     */
    suspend fun getRandomCard(): Carta {
        try {
            val apiCard = apiService.getRandomCard()
            // Convierte el ApiCard (de la red) a Carta (de nuestro dominio)
            return Carta(
                name = apiCard.name,
                type = apiCard.type,
                imageUrl = apiCard.cardImages.first().imageUrl
            )
        } catch (e: Exception) {
            // Manejo de errores: si la red falla, devuelve una carta de error.
            return Carta(
                name = "Error de red",
                type = "No se pudo obtener una carta",
                imageUrl = "" // URL vacía o una imagen de error local
            )
        }
    }

    /**
     * Devuelve una lista de Tiendas que tienen esa carta en su stock.
     * Esta es la única versión de findStoresForCard que necesitas.
     */
    suspend fun findStoresForCard(card: Carta): List<Tienda> {
        // Filtra la lista local de tiendas para ver si alguna contiene una carta con el mismo nombre.
        return localStores.filter { store -> store.cards.any { it.name == card.name } }
    }

    // 2. Todas las funciones duplicadas y las que usaban 'allCards' y 'allStores' han sido eliminadas.
}
