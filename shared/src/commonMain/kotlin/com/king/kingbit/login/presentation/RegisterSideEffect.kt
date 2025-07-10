package com.king.kingbit.login.presentation

sealed class RegisterSideEffect {
    data object Idle: RegisterSideEffect()
    data class RegisterStatus(val isRegisterSuccess : Boolean) : RegisterSideEffect()
}