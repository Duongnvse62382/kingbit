package com.king.kingbit.login.presentation

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    object LoginSuccess : LoginState
    object LoginFailed : LoginState
    object RegisterSuccess : LoginState
    object RegisterFailed : LoginState
}