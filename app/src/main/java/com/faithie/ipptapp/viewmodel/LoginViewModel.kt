package com.faithie.ipptapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithie.ipptapp.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun signIn(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            // Simulate a delay to mimic network request
            kotlinx.coroutines.delay(1000)
            _loginState.value = LoginState.Success(
                User(
                id = "2834703",
                email = "example@gmail.com",
                username =  "example"
            )
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}