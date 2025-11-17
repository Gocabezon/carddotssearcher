package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import kotlin.random.Random // Necesario para getRandomCards

class CardRepository {

    private val allCards = listOf(
        // ✅ CORREGIDO: El modelo es Carta(name: String, imageRes: Int)
        Carta("Mago Oscuro", R.drawable.magooscuro),
        Carta("Dragón Blanco de Ojos Azules", R.drawable.dragonblancoojosazules),
        Carta("Exodia, el Prohibido", R.drawable.exodiaprohibido),
        Carta("Jurrac Meteor", R.drawable.jurracmeteor)
    )

    private val allStores = listOf(
        // Aquí se asumen campos del modelo Tienda que no has proporcionado
        Tienda("Tienda 1", listOf(allCards[0], allCards[2]), 1,R.drawable.tienda1, -33.45694, -70.64827),
        Tienda("Tienda 2", listOf(allCards[1], allCards[3]),0,R.drawable.tienda2, -33.44889, -70.66926),
        Tienda("Tienda 3", allCards,3,R.drawable.tienda3, -33.43792, -70.65032)
    )

    /**
     * Devuelve una lista de Tiendas que tienen esa carta en su stock.
     */
    fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { it.cards.contains(card) }
    }

    // Función auxiliar para obtener todas las cartas disponibles
    fun obtenerTodasLasCartas(): List<Carta> {
        return allCards
    }

    // Función auxiliar para obtener todas las tiendas
    fun obtenerTodasLasTiendas(): List<Tienda> {
        return allStores
    }

    fun getRandomCard(): Carta {
        return allCards.random()
    }

    /**
     * Devuelve N cartas aleatorias. (Método que usa tu ViewModel)
     */
    fun getRandomCards(count: Int): List<Carta> {
        val posts = obtenerTodasLasCartas()
        if (posts.isEmpty()) return emptyList()
        return posts.shuffled(Random).take(count)
    }

    /**
     * Búsqueda por nombre (necesaria para handleSearch en el ViewModel)
     */
    fun searchCards(query: String): List<Carta> {
        val lowerCaseQuery = query.lowercase()
        return allCards.filter {
            // ✅ Usa el campo 'name' del modelo Carta
            it.name.lowercase().contains(lowerCaseQuery)
        }
    }
}