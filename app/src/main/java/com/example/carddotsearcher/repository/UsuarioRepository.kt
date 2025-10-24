package com.example.carddotsearcher.repository

import com.example.carddotsearcher.model.Usuario

class UsuarioRepository {

    private val users = mutableListOf(
        Usuario("admin", "admin")
    )

    fun findUser(username: String, password: String): Usuario? {
        return users.find { it.username == username && it.password == password }
    }

    fun registerUser(username: String, password: String): Boolean {
        if (users.any { it.username == username }) {
            return false // El usuario ya existe
        }
        users.add(Usuario(username, password))
        return true
    }
}
