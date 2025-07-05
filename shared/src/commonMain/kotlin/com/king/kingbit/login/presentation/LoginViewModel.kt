package com.king.kingbit.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _eventAuth = MutableSharedFlow<AuthenticationEvent>()
    val eventAuth = _eventAuth.asSharedFlow()

    private val _eventRegister = MutableSharedFlow<RegisterEvent>()
    val eventRegister = _eventRegister.asSharedFlow()

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
                    _eventAuth.emit(AuthenticationEvent.Idle)
                    _eventRegister.emit(RegisterEvent.Idle)
                }

                LoginAction.GoRegister -> {
                    _eventAuth.emit(AuthenticationEvent.NavigationRegister)
                }

                is LoginAction.CheckEmailExited -> {
                    checkUserExit(loginAction.username)
                }
            }
        }
    }

    private suspend fun login(username: String, password: String) {
        delay(1000)
        val result = repository.login(username, password)
        if (result) {
            _eventAuth.emit(AuthenticationEvent.NavigateHome)
        } else {
            _eventAuth.emit(AuthenticationEvent.ShowError("Login Fail"))
        }
    }

    private suspend fun register(username: String, password: String) {
        val result = repository.register(username, password)
        if (result) {
            _eventAuth.emit(AuthenticationEvent.NavigateHome)
        } else {
            _eventAuth.emit(AuthenticationEvent.ShowError("register Fail"))
        }
    }

    private suspend fun checkUserExit(username: String) {
        val result = repository.isUserExists(username)
        if(result) {
            _eventRegister.emit(RegisterEvent.ShowError("User has exit"))
        } else {
            _eventRegister.emit(RegisterEvent.NextPasswordStep)
        }
    }
}