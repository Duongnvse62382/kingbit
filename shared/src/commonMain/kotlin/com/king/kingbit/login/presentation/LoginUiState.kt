package com.king.kingbit.login.presentation

sealed class LoginEvent {
    data object Idle: LoginEvent()
    data class ShowError(val message: String): LoginEvent()
    data object NavigateHome: LoginEvent()
    data object NavigationRegister : LoginEvent()
}