package com.example.carddotsearcher.repository

import android.content.Context
import com.example.carddotsearcher.data.UserStorageService
import com.example.carddotsearcher.model.Usuario

class UserRepository(context: Context) {

    private val storageService = UserStorageService(context)

    /**
     * Registra un nuevo usuario.
     */
    fun register(newUser: Usuario): Boolean {
        return storageService.addUser(newUser)
    }

    /**
     * Valida las credenciales de un usuario.
     * Devuelve el objeto Usuario si el login es correcto, o null si falla.
     */
    fun login(credentials: Usuario): Usuario? {
        val users = storageService.readUsers()
        return users.find {
            it.username.equals(credentials.username, ignoreCase = true) && it.password == credentials.password
        }
    }
}
