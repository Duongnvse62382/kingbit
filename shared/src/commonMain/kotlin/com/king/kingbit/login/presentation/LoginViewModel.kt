package com.king.kingbit.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()

    fun onAction(loginAction: LoginAction) {
        viewModelScope.launch {
            when (loginAction) {
                is LoginAction.LoginKingBit -> {
                    login(loginAction.username, loginAction.password)
                }

                is LoginAction.RegisterKingBit -> {
                    register(loginAction.username, loginAction.password)
                }

                is LoginAction.ResetLogin -> {
                    _event.emit(LoginEvent.Idle)
                }
            }
        }
    }

    private suspend fun login(username: String, password: String) {
        delay(1000)
        val result = repository.login(username, password)
        if (result) {
            _event.emit(LoginEvent.NavigateHome)
        } else {
            _event.emit(LoginEvent.ShowError("Login Fail"))
        }
    }

    private suspend fun register(username: String, password: String) {
        val result = repository.register(username, password)
        if (result) {
            _event.emit(LoginEvent.NavigateHome)
        } else {
            _event.emit(LoginEvent.ShowError("register Fail"))
        }
    }
}