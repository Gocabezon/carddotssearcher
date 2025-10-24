package com.example.carddotsearcher.repository

import com.example.carddotsearcher.R
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda

class CardRepository {

    private val allCards = listOf(
        Carta("Mago Oscuro", R.drawable.magooscuro),
        Carta("Dragón Blanco de Ojos Azules", R.drawable.dragonblancoojosazules),
        Carta("Exodia, el Prohibido", R.drawable.exodiaprohibido),
        Carta("Jurrac Meteor", R.drawable.jurracmeteor)
    )

    private val allStores = listOf(
        Tienda("Tienda 1", listOf(allCards[0], allCards[2]), R.drawable.tienda1, -33.45694, -70.64827),
        Tienda("Tienda 2", listOf(allCards[1]), R.drawable.tienda2, -33.44889, -70.66926),
        Tienda("Tienda 3", allCards, R.drawable.tienda3, -33.43792, -70.65032)
    )

    fun findStoresForCard(card: Carta): List<Tienda> {
        return allStores.filter { it.cards.contains(card) }
    }

    fun getRandomCard(): Carta {
        return allCards.random()
    }
}
