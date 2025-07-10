package com.king.kingbit.login.presentation

sealed class AuthenticationSideEffect {
    data object Idle: AuthenticationSideEffect()
    data class ShowError(val message: String): AuthenticationSideEffect()
    data object NavigateHome: AuthenticationSideEffect()
    data object NavigationRegister : AuthenticationSideEffect()
}