package com.example.carddotsearcher.data

import android.content.Context
import com.example.carddotsearcher.model.Usuario
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class UserStorageService(private val context: Context) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Adaptador para convertir una Lista de Usuarios a JSON y viceversa
    private val userListAdapter = moshi.adapter<List<Usuario>>(
        Types.newParameterizedType(List::class.java, Usuario::class.java)
    )

    private val userFile = File(context.filesDir, "users.json")

    /**
     * Lee todos los usuarios del archivo users.json.
     * Si el archivo no existe, devuelve una lista vacía.
     */
    fun readUsers(): List<Usuario> {
        if (!userFile.exists()) {
            return emptyList()
        }
        val json = userFile.readText()
        return if (json.isBlank()) emptyList() else userListAdapter.fromJson(json) ?: emptyList()
    }

    /**
     * Escribe la lista completa de usuarios en el archivo users.json,
     * sobrescribiendo el contenido anterior.
     */
    fun writeUsers(users: List<Usuario>) {
        val json = userListAdapter.toJson(users)
        userFile.writeText(json)
    }

    /**
     * Añade un nuevo usuario a la lista y guarda el archivo.
     * Devuelve true si se añadió con éxito, false si el usuario ya existía.
     */
    fun addUser(newUser: Usuario): Boolean {
        val currentUsers = readUsers().toMutableList()
        // Comprueba si ya existe un usuario con el mismo nombre
        if (currentUsers.any { it.username.equals(newUser.username, ignoreCase = true) }) {
            return false // El usuario ya existe
        }
        currentUsers.add(newUser)
        writeUsers(currentUsers)
        return true
    }
}
