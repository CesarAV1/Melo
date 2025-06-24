package com.example.melo.repository

import android.content.Context
import com.example.melo.database.UserDatabase
import com.example.melo.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepository(context: Context) {
    private val userDatabase = UserDatabase(context)
    private val _currentUser = MutableStateFlow<User?>(null)

    val currentUser: StateFlow<User?> = _currentUser

    init {
        _currentUser.value = userDatabase.getCurrentUser()
    }

    fun registerUser(name: String, email: String, password: String): Boolean {
        val newUser = User(
            id = userDatabase.generateUserId(),
            name = name,
            email = email,
            password = password
        )

        val success = userDatabase.saveUser(newUser)
        if (success) {
            _currentUser.value = newUser
            userDatabase.saveCurrentUser(newUser)
        }
        return success
    }

    fun loginUser(email: String, password: String): Boolean {
        val user = userDatabase.findUserByEmailAndPassword(email, password)
        return if (user != null) {
            _currentUser.value = user
            userDatabase.saveCurrentUser(user)
            true
        } else {
            false
        }
    }

    fun logoutUser() {
        _currentUser.value = null
        userDatabase.clearCurrentUser()
    }

    fun getAllUsers(): List<User> {
        return userDatabase.getAllUsers()
    }
}
