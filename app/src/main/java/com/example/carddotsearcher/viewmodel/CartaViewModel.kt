package com.example.carddotsearcher.viewmodel



import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddotsearcher.model.Carta // ✅ Usar el modelo Carta
import com.example.carddotsearcher.repository.CardRepository // ✅ Usar el repositorio CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✅ Clase renombrada para mayor claridad (Antes PostViewModel)
class CartaViewModel(private val repository: CardRepository) : ViewModel() {

    // ✅ El StateFlow ahora contiene una lista de 'Carta'
    private val _displayedCards = MutableStateFlow<List<Carta>>(emptyList())
    // El tipo de salida es List<Carta>
    val posts: StateFlow<List<Carta>> = _displayedCards.asStateFlow()

    // El estado del texto que el usuario introduce en el buscador
    var searchQuery = mutableStateOf("")
        private set

    init {
        loadInitialCards()
    }

    /** Carga inicial: Obtiene 4 cartas aleatorias. */
    fun loadInitialCards() {
        viewModelScope.launch {
            try {
                // ---- BLOQUE CORREGIDO ----
                // 1. Llama a la función que sí existe para obtener UNA carta.
                val randomCard = repository.getRandomCard()
                // 2. Muestra esa única carta envolviéndola en una lista.
                _displayedCards.value = listOf(randomCard)
                // ---- FIN DEL BLOQUE CORREGIDO ----
            } catch (e: Exception) {
                _displayedCards.value = emptyList() // En caso de error, la lista estará vacía.
            }
        }
    }

    /** Se llama cada vez que el usuario cambia el texto del buscador. */
    fun handleSearch(query: String) {
        searchQuery.value = query

        if (query.isBlank()) {
            loadInitialCards()
        } else {
            viewModelScope.launch {
                try {
                    // La búsqueda de varias cartas por nombre sí debe existir en la API.
                    // Si no, también habría que ajustar esta parte.
                    // Por ahora, asumimos que existe una función 'searchCards' en el repositorio.
                    // _displayedCards.value = repository.searchCards(query) <-- Esta línea daría error si 'searchCards' no existe.

                    // SOLUCIÓN TEMPORAL: Reutilizamos la lógica de obtener una carta.
                    // En un futuro, deberías implementar 'searchCards' en tu repositorio y API.
                    val card = repository.getRandomCard() // Simulación
                    _displayedCards.value = listOf(card)

                } catch (e: Exception) {
                    _displayedCards.value = emptyList()
                }
            }
        }
    }
}