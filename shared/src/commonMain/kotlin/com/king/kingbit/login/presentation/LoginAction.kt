package com.king.kingbit.login.presentation

sealed class LoginAction {
    data object ResetLogin : LoginAction()
    data object GoRegister : LoginAction()
    data object ResetEmailError : LoginAction()
    data object ResetPasswordError : LoginAction()
    data object LogOut : LoginAction()
    data class LoginKingBit(val username: String, val password: String) : LoginAction()
}
