package com.example.melo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.melo.model.User
import com.example.melo.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    val currentUser: StateFlow<User?> = userRepository.currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                _isLoggedIn.value = user != null
            }
        }
    }

    fun onEmailChange(email: String) {
        _email.value = email
        _loginError.value = null
        _registerError.value = null
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        _loginError.value = null
        _registerError.value = null
    }

    fun onNameChange(name: String) {
        _name.value = name
        _registerError.value = null
    }

    fun login(): Boolean {
        return if (_email.value.isNotEmpty() && _password.value.isNotEmpty()) {
            val success = userRepository.loginUser(_email.value, _password.value)
            if (!success) {
                _loginError.value = "Credenciales incorrectas"
            } else {
                clearFields()
            }
            success
        } else {
            _loginError.value = "Por favor completa todos los campos"
            false
        }
    }

    fun register(): Boolean {
        return if (_email.value.isNotEmpty() && _password.value.isNotEmpty() && _name.value.isNotEmpty()) {
            val success = userRepository.registerUser(_name.value, _email.value, _password.value)
            if (!success) {
                _registerError.value = "El email ya está registrado"
            } else {
                clearFields()
            }
            success
        } else {
            _registerError.value = "Por favor completa todos los campos"
            false
        }
    }

    fun logout() {
        userRepository.logoutUser()
        clearFields()
    }

    private fun clearFields() {
        _email.value = ""
        _password.value = ""
        _name.value = ""
        _loginError.value = null
        _registerError.value = null
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }
}
