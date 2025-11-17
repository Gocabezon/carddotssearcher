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
                // ✅ Carga 4 cartas aleatorias.
                _displayedCards.value = repository.getRandomCards(4)
            } catch (e: Exception) { /* Manejo de errores */ }
        }
    }

    /** Se llama cada vez que el usuario cambia el texto del buscador. */
    fun handleSearch(query: String) {
        searchQuery.value = query // Actualiza el estado del texto del buscador

        if (query.isBlank()) {
            loadInitialCards() // Si la búsqueda está vacía, vuelve a cargar las 4 aleatorias
        } else {
            // Lanza una coroutine para buscar y filtrar
            viewModelScope.launch {
                try {
                    // ✅ Llama al método searchCards de CardRepository
                    _displayedCards.value = repository.searchCards(query)
                } catch (e: Exception) { /* Manejo de errores */ }
            }
        }
    }
}