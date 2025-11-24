package com.example.carddotsearcher.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.repository.CardRepository
import com.example.carddotsearcher.repository.MockCardDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartaViewModel : ViewModel() {

    // Se crea una instancia del repositorio aquí mismo
    private val repository = CardRepository()

    private val _displayedCards = MutableStateFlow<List<Carta>>(emptyList())
    val posts: StateFlow<List<Carta>> = _displayedCards.asStateFlow()

    var searchQuery = mutableStateOf("")
        private set

    init {
        // Al iniciar, cargamos una carta aleatoria para que la pantalla no esté vacía
        loadInitialCard()
    }

    /** Carga una carta aleatoria inicial. */
    private fun loadInitialCard() {
        viewModelScope.launch {
            try {
                // 1. Obtiene un nombre aleatorio de la lista local.
                val randomCardName = MockCardDataSource.mockCards.random().name
                // 2. Llama a la función de búsqueda que se conecta a la API.
                val randomCard = repository.searchCardByName(randomCardName)
                // 3. Muestra la carta si se encontró.
                _displayedCards.value = if (randomCard != null) listOf(randomCard) else emptyList()
            } catch (e: Exception) {
                // En caso de error, la lista se queda vacía
                _displayedCards.value = emptyList()
            }
        }
    }

    /** Se llama cada vez que el usuario cambia el texto del buscador. */
    fun handleSearch(query: String) {
        searchQuery.value = query

        // Si el buscador está vacío, volvemos a cargar una carta aleatoria
        if (query.isBlank()) {
            loadInitialCard()
        } else {
            // Si hay texto, buscamos esa carta específica en la API
            viewModelScope.launch {
                try {
                    val card = repository.searchCardByName(query)
                    _displayedCards.value = if (card != null) listOf(card) else emptyList()
                } catch (e: Exception) {
                    _displayedCards.value = emptyList()
                }
            }
        }
    }
}
