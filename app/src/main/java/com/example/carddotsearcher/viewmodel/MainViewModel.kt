package com.example.carddotsearcher.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddotsearcher.model.Carta
import com.example.carddotsearcher.model.Tienda
import com.example.carddotsearcher.repository.CardRepository
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
            _isLoading.value = true
            delay(1500) // Simulate network delay

            val randomCard = repository.getRandomCard()
            _selectedCard.value = randomCard

            val stores = repository.findStoresForCard(randomCard)
            _foundStores.value = stores

            // Add to history
            val currentHistory = _searchHistory.value ?: emptyList()
            _searchHistory.value = listOf(randomCard) + currentHistory

            _isLoading.value = false
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

    fun getAllStores(): List<Tienda> {
        return repository.getAllStores() // Llama a la nueva función pública del repositorio
    }
}
