package com.example.melo.database

import android.content.Context
import android.content.SharedPreferences
import com.example.melo.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserDatabase(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cine_melo_users", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val USERS_KEY = "users"
        private const val CURRENT_USER_KEY = "current_user"
    }

    init {
        if (getAllUsers().isEmpty()) {
            addTestUser()
        }
    }

    private fun addTestUser() {
        val testUser = User(
            id = "test_user_001",
            name = "Juan Pérez",
            email = "juan@test.com",
            password = "123456"
        )
        saveUser(testUser)
    }

    fun saveUser(user: User): Boolean {
        val users = getAllUsers().toMutableList()

        if (users.any { it.email == user.email }) {
            return false
        }

        users.add(user)
        val usersJson = gson.toJson(users)
        sharedPreferences.edit().putString(USERS_KEY, usersJson).apply()
        return true
    }

    fun getAllUsers(): List<User> {
        val usersJson = sharedPreferences.getString(USERS_KEY, null)
        return if (usersJson != null) {
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(usersJson, type)
        } else {
            emptyList()
        }
    }

    fun findUserByEmailAndPassword(email: String, password: String): User? {
        return getAllUsers().find { it.email == email && it.password == password }
    }

    fun saveCurrentUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(CURRENT_USER_KEY, userJson).apply()
    }

    fun getCurrentUser(): User? {
        val userJson = sharedPreferences.getString(CURRENT_USER_KEY, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearCurrentUser() {
        sharedPreferences.edit().remove(CURRENT_USER_KEY).apply()
    }

    fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}"
    }
}
