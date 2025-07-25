package com.king.kingbit.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.login.domain.model.User
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow(User())
    val userState = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUserAuthenticated().collect { userEntity ->
                userEntity?.let {
                    _userState.update {
                        it.copy(
                            id = userEntity.id,
                            username = userEntity.username,
                            isAuthenticated = userEntity.isAuthenticated
                        )
                    }
                }
            }
        }
    }

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
        delay(500)
        val result = repository.login(username, password)
        if (result) {
            _eventAuth.emit(AuthenticationEvent.NavigateHome)
        } else {
            _eventAuth.emit(AuthenticationEvent.ShowError("The email or password you entered is incorrect. Please try again."))
        }
    }

    private suspend fun register(username: String, password: String) {
        val result = repository.register(username, password)
        if (result) {
            _eventRegister.emit(RegisterEvent.RegisterStatus(result))
        } else {
            _eventRegister.emit(RegisterEvent.RegisterStatus(result))
        }
    }

    private suspend fun checkUserExit(username: String) {
        val result = repository.isUserExists(username)
        if (result) {
            _eventRegister.emit(RegisterEvent.ShowError("An account with this email already exists. Please log in or use a different email."))
        } else {
            _eventRegister.emit(RegisterEvent.NextPasswordStep)
        }
    }
}