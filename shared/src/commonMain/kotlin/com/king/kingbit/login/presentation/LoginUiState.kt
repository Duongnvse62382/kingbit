package com.king.kingbit.login.presentation

data class LoginUIState(val emailBlank : Boolean = false, val passwordBlank : Boolean = false, val emailInValid : Boolean = false)

sealed class LoginEvent {
    data object Idle: LoginEvent()
    data class ShowError(val message: String): LoginEvent()
    data object NavigateHome: LoginEvent()
}