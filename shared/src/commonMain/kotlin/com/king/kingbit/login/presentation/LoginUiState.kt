package com.king.kingbit.login.presentation

sealed class AuthenticationEvent {
    data object Idle: AuthenticationEvent()
    data class ShowError(val message: String): AuthenticationEvent()
    data object NavigateHome: AuthenticationEvent()
    data object NavigationRegister : AuthenticationEvent()
}

sealed class RegisterEvent {
    data object Idle: RegisterEvent()
    data class ShowError(val message: String): RegisterEvent()
    data object NextPasswordStep: RegisterEvent()
}