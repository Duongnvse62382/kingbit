package com.king.kingbit.login.presentation

sealed class LoginAction {
    data object ResetLogin : LoginAction()
    data object GoRegister : LoginAction()
    data class CheckEmailExited(val username: String) : LoginAction()
    data class LoginKingBit(val username: String, val password: String) : LoginAction()
    data class RegisterKingBit(val username: String, val password: String) : LoginAction()
}
