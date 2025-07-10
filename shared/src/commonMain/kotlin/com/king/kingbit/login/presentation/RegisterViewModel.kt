package com.king.kingbit.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.kingbit.login.domain.usecase.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _eventRegister = MutableSharedFlow<RegisterSideEffect>()
    val eventRegister = _eventRegister.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm = _passwordConfirm.asStateFlow()
    private val _step = MutableStateFlow(RegisterStep.EMAIL)
    val step = _step.asStateFlow()
    private val _emailError = MutableStateFlow("")
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError = _passwordError.asStateFlow()

    fun onAction(registerAction: RegisterAction) {
        viewModelScope.launch {
            when (registerAction) {
                is RegisterAction.EmailChanged -> {
                    _email.value = registerAction.value
                    _emailError.value = ""
                }

                is RegisterAction.PasswordChanged -> {
                    _password.value = registerAction.value
                    _passwordError.value = ""
                }

                is RegisterAction.PasswordConfirmChanged -> {
                    _passwordConfirm.value = registerAction.value
                    _passwordError.value = ""
                }

                is RegisterAction.CheckEmailExited -> {
                    val exists = userRepository.isUserExists(registerAction.username)
                    if (exists) {
                        _emailError.value = "An account with this email already exists. Please log in or use a different email."
                    } else {
                        _step.value = RegisterStep.PASSWORD
                    }
                }

                is RegisterAction.RegisterKingBit -> {
                    register(registerAction.username, registerAction.password)
                }

                RegisterAction.GoToEmailStep -> {
                    _step.value = RegisterStep.EMAIL
                }

                RegisterAction.ResetRegister -> {
                    _eventRegister.emit(RegisterSideEffect.Idle)
                    _step.value = RegisterStep.EMAIL
                }

                RegisterAction.CheckPasswordMatch -> {
                    if (_password.value != _passwordConfirm.value) {
                        _passwordError.value = "Passwords do not match"
                    } else {
                        _passwordError.value = ""
                    }
                }
            }
        }
    }

    private suspend fun register(username: String, password: String) {
        val result = userRepository.register(username, password)
        _eventRegister.emit(RegisterSideEffect.RegisterStatus(result))
    }

}