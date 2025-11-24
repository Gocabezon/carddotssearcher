package com.example.carddotsearcher.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.repository.CardRepository
import com.example.carddotsearcher.repository.MockCardDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = CardRepository()

    private val _foundStores = MutableLiveData<List<Tienda>>()
    val foundStores: LiveData<List<Tienda>> = _foundStores

    private val _selectedCard = MutableLiveData<Carta>()
    val selectedCard: LiveData<Carta> = _selectedCard

    // For history
    private val _searchHistory = MutableLiveData<List<Carta>>(emptyList())
    val searchHistory: LiveData<List<Carta>> = _searchHistory

    // For loading state
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _photoBitmap = MutableLiveData<Bitmap?>()
    val photoBitmap: LiveData<Bitmap?> = _photoBitmap

    fun setPhotoBitmap(bitmap: Bitmap?) {
        _photoBitmap.value = bitmap
    }

    fun searchRandomCard() {
        viewModelScope.launch {
            // 1. Obtener un nombre de carta aleatorio de la lista local.
            val randomCardName = MockCardDataSource.mockCards.random().name


            searchCardByName(randomCardName)
        }
    }

    fun searchSpecificCard(card: Carta) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1500) // Simulate network delay

            _selectedCard.value = card

            val stores = repository.findStoresForCard(card)
            _foundStores.value = stores

            _isLoading.value = false
        }
    }

    fun searchCardByName(cardName: String) {
        if (cardName.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true


            // 1. Llama a la función del repositorio que se conecta a la API.
            val foundCard = repository.searchCardByName(cardName)

            if (foundCard != null) {
                // 2. Si la API encontró la carta, actualiza la UI.
                _selectedCard.value = foundCard
                _foundStores.value = repository.findStoresForCard(foundCard)
                // 3. Añade la carta al historial.
                addCardToHistory(foundCard)
            }
            // Si 'foundCard' es null, no se hace nada y el usuario no ve cambios.
            _isLoading.value = false
        }
    }

    private fun addCardToHistory(card: Carta) {
        val currentHistory = _searchHistory.value ?: emptyList()
        if (!currentHistory.any { it.name == card.name }) {
            _searchHistory.value = listOf(card) + currentHistory
        }
    }

    fun getAllStores(): List<Tienda> {
        return repository.allStores // Llama a la nueva función pública del repositorio
    }
}
