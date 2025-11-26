package com.example.carddotsearcher.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carddotsearcher.model.Usuario
import com.example.carddotsearcher.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    // Usaremos SharedPreferences para guardar el nombre del usuario logueado
    private val sharedPreferences = application.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    private val _loggedInUser = MutableStateFlow<String?>(null)
    val loggedInUser = _loggedInUser.asStateFlow()

    // Estados para la UI: Idle (inicial), Loading (cargando), Success (éxito), Error (fallo)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
    enum class AuthState { Idle, Loading, Success, Error }

    init {
        // Al iniciar el ViewModel, comprueba si ya hay una sesión guardada
        _loggedInUser.value = sharedPreferences.getString("LOGGED_IN_USERNAME", null)
    }

    fun register(user: Usuario) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = userRepository.register(user)
            if (success) {
                // Si el registro es exitoso, automáticamente inicia sesión
                login(user)
            } else {
                _authState.value = AuthState.Error // El usuario ya existe
            }
        }
    }

    fun login(credentials: Usuario) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.login(credentials)
            if (user != null) {
                // ÉXITO: Guarda el usuario en SharedPreferences y actualiza el estado
                sharedPreferences.edit().putString("LOGGED_IN_USERNAME", user.username).apply()
                _loggedInUser.value = user.username
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error // Credenciales incorrectas
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // BORRAR: Limpia SharedPreferences y actualiza el estado
            sharedPreferences.edit().remove("LOGGED_IN_USERNAME").apply()
            _loggedInUser.value = null
        }
    }

    // Función para resetear el estado de la UI
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
