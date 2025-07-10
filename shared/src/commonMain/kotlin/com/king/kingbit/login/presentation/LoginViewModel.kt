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

    private val _emailError = MutableStateFlow("")
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError = _passwordError.asStateFlow()

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

    private val _eventAuth = MutableSharedFlow<AuthenticationSideEffect>()
    val eventAuth = _eventAuth.asSharedFlow()


    fun onAction(loginAction: LoginAction) {
        viewModelScope.launch {
            when (loginAction) {
                is LoginAction.LoginKingBit -> {
                    onLogin(loginAction.username, loginAction.password)
                }

                is LoginAction.ResetLogin -> {
                    _eventAuth.emit(AuthenticationSideEffect.Idle)
                }

                LoginAction.GoRegister -> {
                    _eventAuth.emit(AuthenticationSideEffect.NavigationRegister)
                }

                is LoginAction.ResetEmailError -> {
                    _emailError.value = ""
                }

                is LoginAction.ResetPasswordError -> {
                    _passwordError.value = ""
                }

                LoginAction.LogOut -> {
                    repository.logoutUser("duong@gmail.com")
                    _userState.update {
                        it.copy(
                            isAuthenticated = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun onLogin(email: String, password: String) {
        val (emailError, passwordError) = validateLogin(email, password)
        _emailError.value = emailError
        _passwordError.value = passwordError
        if (emailError.isEmpty() && passwordError.isEmpty()) {
            login(email, password)
        }
    }

    private fun validateLogin(email: String, password: String): Pair<String, String> {
        val emailError = when {
            email.isEmpty() -> "Please enter your email address."
            !isValidEmail(email) -> "Enter a valid email address."
            else -> ""
        }
        val passwordError = if (password.isEmpty()) "Please enter your password." else ""
        return emailError to passwordError
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private suspend fun login(username: String, password: String) {
        delay(500)
        val result = repository.login(username, password)
        if (result) {
            _eventAuth.emit(AuthenticationSideEffect.NavigateHome)
        } else {
            _eventAuth.emit(AuthenticationSideEffect.ShowError("The email or password you entered is incorrect. Please try again."))
        }
    }
}