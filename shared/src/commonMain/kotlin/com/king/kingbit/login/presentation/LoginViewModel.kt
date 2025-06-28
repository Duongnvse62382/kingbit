package com.king.kingbit.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = if (repository.login(username, password)) LoginState.LoginSuccess
            else LoginState.LoginFailed
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = if (repository.register(username, password)) LoginState.RegisterSuccess
            else LoginState.RegisterFailed
        }
    }
}