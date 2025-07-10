package com.king.kingbit.login.presentation

sealed class RegisterAction {
    data object ResetRegister : RegisterAction()
    data class EmailChanged(val value: String) : RegisterAction()
    data class PasswordChanged(val value: String) : RegisterAction()
    data class PasswordConfirmChanged(val value: String) : RegisterAction()
    data object CheckPasswordMatch : RegisterAction()
    data object GoToEmailStep : RegisterAction()
    data class RegisterKingBit(val username: String, val password: String) : RegisterAction()
    data class CheckEmailExited(val username: String) : RegisterAction()
}