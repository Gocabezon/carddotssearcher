package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda

class CardRepository {

    private val allCards = listOf(
        Carta("Mago Oscuro"),
        Carta("Dragón Blanco de Ojos Azules"),
        Carta("Exodia, el Prohibido"),
        Carta("Red-Eyes Black Dragon")
    )

    private val allStores = listOf(
        Tienda("Tienda 1", listOf(allCards[0], allCards[2]), R.drawable.ic_launcher_background),
        Tienda("Tienda 2", listOf(allCards[1]), R.drawable.ic_launcher_foreground),
        Tienda("Tienda 3", allCards, R.drawable.ic_launcher_background)
    )

    fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { it.cards.contains(card) }
    }

    fun getRandomCard(): Carta {
        return allCards.random()
    }
}
